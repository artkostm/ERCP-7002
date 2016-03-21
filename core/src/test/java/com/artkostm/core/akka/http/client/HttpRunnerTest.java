package com.artkostm.core.akka.http.client;

import akka.actor.ActorSystem;

import com.artkostm.core.akka.http.client.common.HttpEndpoint;

public class HttpRunnerTest
{
    public static void main(String[] args) throws Exception
    {
        final ActorSystem system = ActorSystem.create("http-client");
        final HttpEndpoint client = new HttpEndpoint(system);

        final long start = System.currentTimeMillis();
        final String sb1 = client.createFlow("www.bsuir.by", 80)
                .create("/schedule/schedule.xhtml?id=272301").asString();
        System.out.println(sb1);
        
        for (int j = 0; j < 100; j++){
            final String sb2 = client.create("/ru/bguir-eto-").asString();
            System.out.println(sb2);
        }
        System.out.println("Time: " + (System.currentTimeMillis() - start));
//              .onComplete(new HttpEndpoint.Completing(system, client.materializer(), true),
//                  system.dispatcher());
    }
}
