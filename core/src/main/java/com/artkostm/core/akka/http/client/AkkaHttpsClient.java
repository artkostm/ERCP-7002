package com.artkostm.core.akka.http.client;

import scala.concurrent.Future;
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

import com.artkostm.core.akka.http.client.AkkaHttpClient.Completing;

public class AkkaHttpsClient
{
    static long start = 0;
    public static void main(String[] args) throws InterruptedException
    {
        final ActorSystem system = ActorSystem.create("akka-http-client");
        final ActorMaterializer materializer = ActorMaterializer.create(system);
        final Http http = Http.get(system);
        http.defaultClientHttpsContext();
        final Flow<HttpRequest, HttpResponse, Future<OutgoingConnection>> connectionFlow = 
                http.outgoingConnectionTls("raw.githubusercontent.com", 443);//http://artkostm.github.io/switz/ //https://raw.githubusercontent.com/artkostm/test/master/README.md
        start = System.currentTimeMillis();
        final HttpRequest request = HttpRequest.create("/artkostm/test/master/README.md");
        
        final Future<HttpResponse> responseFuture =  Source.single(request)
                .via(connectionFlow)
                .runWith(Sink.<HttpResponse>head(), materializer);
        
        final OnComplete<HttpResponse> onComplete = new Completing(system, materializer);
        
//        Source.single(HttpRequest.create("/news/2016/02/20/4-1-0-CR3.html"))
//            .via(connectionFlow)
//            .runWith(Sink.<HttpResponse>head(), materializer).onComplete(onComplete, system.dispatcher());
        responseFuture.onComplete(onComplete, system.dispatcher());
        Thread.sleep(20000);
        system.terminate();
    }
}
