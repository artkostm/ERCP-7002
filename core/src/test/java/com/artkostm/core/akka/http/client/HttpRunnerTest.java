package com.artkostm.core.akka.http.client;

import akka.actor.ActorSystem;
import akka.stream.javadsl.Sink;
import akka.util.ByteString;

import com.artkostm.core.akka.http.client.common.HttpEndpoint;

public class HttpRunnerTest
{
    public static final StringBuffer sb = new StringBuffer();
    
    public static void main(String[] args) throws Exception
    {
        final ActorSystem system = ActorSystem.create("http-client");
        final HttpEndpoint client = new HttpEndpoint(system);
        client.createFlow("www.bsuir.by", 80)
                .request("/schedule/schedule.xhtml?id=272301").asString();
        
        client.request("/schedule/schedule.xhtml?id=272301", (r, t) -> 
        {
            r.entity().getDataBytes()
                .fold(ByteString.empty(), (z, i) -> z.concat(i))
                .runWith(Sink.<ByteString>head(), client.materializer())
                .whenCompleteAsync((s, e) -> sb.append(s.utf8String()), system.dispatcher());
        });
        
        Thread.sleep(3000);
        System.out.println(sb);
    }
}
