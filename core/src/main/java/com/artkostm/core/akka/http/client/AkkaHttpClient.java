package com.artkostm.core.akka.http.client;

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
    public static void main1(String[] args)
    {
        final ActorSystem system = ActorSystem.create("akka-http-client");
        final ActorMaterializer materializer = ActorMaterializer.create(system);
        final Flow<HttpRequest, HttpResponse, Future<OutgoingConnection>> connectionFlow = 
                Http.get(system).outgoingConnection("netty.io", 80);
        final Future<HttpResponse> responseFuture =  Source.single(HttpRequest.create("/wiki/user-guide-for-5.x.html"))
                .via(connectionFlow)
                .runWith(Sink.<HttpResponse>head(), materializer);
        
        final OnComplete<HttpResponse> onComplete = new Completing(system, materializer);
        
        responseFuture.onComplete(onComplete, system.dispatcher());
    }
    
    static class Completing extends OnComplete<HttpResponse>
    {
        final ActorSystem system;
        final ActorMaterializer materializer;
        
        public Completing(final ActorSystem system, final ActorMaterializer materializer)
        {
            this.materializer = materializer;
            this.system = system;
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
                
                final String sb = Await.result(data, Duration.Inf()).decodeString("UTF-8");
                System.out.println(sb);
                
            }
            finally
            {
                system.terminate();
            }
        }
     };
}
