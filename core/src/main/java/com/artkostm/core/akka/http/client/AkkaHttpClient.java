package com.artkostm.core.akka.http.client;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;
import akka.actor.ActorSystem;
import akka.dispatch.OnComplete;
import akka.http.javadsl.Http;
import akka.http.javadsl.OutgoingConnection;
import akka.http.javadsl.model.HttpRequest;
import akka.http.javadsl.model.HttpResponse;
import akka.stream.ActorMaterializer;
import akka.stream.javadsl.Flow;
import akka.stream.javadsl.Sink;
import akka.stream.javadsl.Source;
import akka.util.ByteString;

public class AkkaHttpClient
{
    static long start = 0;
    public static void main(String[] args) throws InterruptedException
    {
        final ActorSystem system = ActorSystem.create("akka-http-client");
        final ActorMaterializer materializer = ActorMaterializer.create(system);
        final Flow<HttpRequest, HttpResponse, Future<OutgoingConnection>> connectionFlow = 
                Http.get(system).outgoingConnection("netty.io", 80);
        start = System.currentTimeMillis();
        final Future<HttpResponse> responseFuture =  Source.single(HttpRequest.create("/wiki/user-guide-for-5.x.html"))
                .via(connectionFlow)
                .runWith(Sink.<HttpResponse>head(), materializer);
        
        final OnComplete<HttpResponse> onComplete = new Completing(system, materializer, false);
        
        Source.single(HttpRequest.create("/news/2016/02/20/4-1-0-CR3.html"))
            .via(connectionFlow)
            .runWith(Sink.<HttpResponse>head(), materializer).onComplete(onComplete, system.dispatcher());
        responseFuture.onComplete(onComplete, system.dispatcher());
        Thread.sleep(20000);
        system.terminate();
    }
    
    public static class Completing extends OnComplete<HttpResponse>
    {
        final ActorSystem system;
        final ActorMaterializer materializer;
        final boolean shouldTerminateSystem;
        
        public Completing(final ActorSystem system, final ActorMaterializer materializer, final boolean shouldTerminateSystem)
        {
            this.materializer = materializer;
            this.system = system;
            this.shouldTerminateSystem = shouldTerminateSystem;
        }
        
        @Override
        public void onComplete(Throwable arg0, HttpResponse response) throws Throwable
        {
            try
            {
                final Future<ByteString> data = response.entity().getDataBytes().fold(ByteString.empty(), 
                    (z, i) -> 
                    {
                        return z.concat(i);
                    }
                ).runWith(Sink.<ByteString>head(), materializer);
                
                final String sb = Await.result(data, Duration.Inf()).utf8String();
                final Document doc = Jsoup.parse(sb);
                System.out.println(doc.select("body").text());
                System.out.println("Time: " + (System.currentTimeMillis() - start) + "ms");
            }
            finally
            {
                if (shouldTerminateSystem) system.terminate();
            }
        }
     };
}
