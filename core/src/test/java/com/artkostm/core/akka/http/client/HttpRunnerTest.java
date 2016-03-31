package com.artkostm.core.akka.http.client;

import java.io.File;
import java.io.FileWriter;

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

        final long start = System.currentTimeMillis();
        final String sb1 = client.createFlow("www.bsuir.by", 80)
                .request("/schedule/schedule.xhtml?id=272301").asString();
        System.out.println(sb1);
        
        for (int j = 0; j < 1; j++)
        {
            client.request("/schedule/schedule.xhtml?id=272301", (r, t) -> 
            {
                r.entity().getDataBytes()
                    .fold(ByteString.empty(), (z, i) -> z.concat(i))
                    .runWith(Sink.<ByteString>head(), client.materializer())
                    .whenCompleteAsync((s, e) -> sb.append(s.utf8String() + "\nTime: " + (System.currentTimeMillis() - start)), system.dispatcher());
            });
        }
        
        final String sb2 = client.createFlow("www.nurkiewicz.com", 80)
                .request("/2013/05/java-8-definitive-guide-to.html").asString();
        System.out.println(sb2);
        
        for (int j = 0; j < 1; j++)
        {
            client.request("/2013/05/java-8-definitive-guide-to.html", (r, t) -> 
            {
                r.entity().getDataBytes()
                    .fold(ByteString.empty(), (z, i) -> z.concat(i))
                    .runWith(Sink.<ByteString>head(), client.materializer())
                    .whenCompleteAsync((s, e) -> sb.append(s.utf8String() + "\nTime: " + (System.currentTimeMillis() - start)), system.dispatcher());
            });
        }
        System.out.println("Time: " + (System.currentTimeMillis() - start));
        
        Thread.sleep(20000);
        FileWriter writer = new FileWriter(new File("http.requests.html"));
        writer.write(sb.toString());
        writer.flush();
        writer.close();
    }
}
