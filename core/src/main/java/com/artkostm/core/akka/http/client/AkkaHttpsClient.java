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
    public static void main(String[] args)
    {
        final ActorSystem system = ActorSystem.create("akka-http-client");
        final ActorMaterializer materializer = ActorMaterializer.create(system);
        final Http http = Http.get(system);
        http.defaultClientHttpsContext();
        final Flow<HttpRequest, HttpResponse, Future<OutgoingConnection>> connectionFlow = 
                http.outgoingConnectionTls("raw.githubusercontent.com", 443);
        final HttpRequest request = HttpRequest.create("/artkostm/ERCP-7002/master/core/src/main/java/com/artkostm/core/akka/http/client/AkkaHttpsClient.java");
        
        final Future<HttpResponse> responseFuture =  Source.single(request)
                .via(connectionFlow)
                .runWith(Sink.<HttpResponse>head(), materializer);
        
        final OnComplete<HttpResponse> onComplete = new Completing(system, materializer, true);
        
        responseFuture.onComplete(onComplete, system.dispatcher());
    }
}
