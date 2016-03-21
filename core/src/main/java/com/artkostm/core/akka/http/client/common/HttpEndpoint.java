package com.artkostm.core.akka.http.client.common;

import com.artkostm.core.akka.extension.ActorSystemAware;

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

public class HttpEndpoint implements HttpFlowBuilder, HttpRequestRunner,
                                     ResponseHandler,
                                     ActorSystemAware, ActorMaterializerAware
{
    private final ActorSystem system;
    private final ActorMaterializer materializer;
    
    private Flow<HttpRequest, HttpResponse, Future<OutgoingConnection>> connectionFlow;
    private HttpResponse response;
    
    public HttpEndpoint(final ActorSystem system)
    {
        this.system = system;
        materializer = ActorMaterializer.create(system);
    }
    
    @Override
    public ResponseHandler create(String url)
    {
        return create(HttpRequest.create(url));
    }

    @Override
    public ResponseHandler create(HttpRequest request)
    {
        try
        {
            response = Await.result(Source.single(request)
                .via(connectionFlow)
                .runWith(Sink.<HttpResponse>head(), materializer), Duration.Inf());
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
        return this;
    }
    
    @Override
    public Future<HttpResponse> create(String url, OnComplete<HttpResponse> onComplete)
    {
        return create(HttpRequest.create(url), onComplete);
    }

    @Override
    public Future<HttpResponse> create(HttpRequest request, OnComplete<HttpResponse> onComplete)
    {
        final Future<HttpResponse> future = Source.single(request)
                .via(connectionFlow)
                .runWith(Sink.<HttpResponse>head(), materializer);
        future.onComplete(onComplete, system.dispatcher());
        return future;
    };

    @Override
    public HttpRequestRunner createFlow(String host, int port)
    {
        connectionFlow = Http.get(system).outgoingConnection(host, port);
        return this;
    }

    @Override
    public void actorSystem(ActorSystem system)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public ActorSystem actorSystem()
    {
        return system;
    }

    @Override
    public ActorMaterializer materializer()
    {
        return materializer;
    }

    @Override
    public String asString() throws Exception
    {
        final Future<ByteString> data = response.entity().getDataBytes().fold(ByteString.empty(), 
            (z, i) -> 
            {
                return z.concat(i);
            }
        ).runWith(Sink.<ByteString>head(), materializer);
        return Await.result(data, Duration.Inf()).utf8String();
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
                System.out.println(sb);
            }
            finally
            {
                if (shouldTerminateSystem) system.terminate();
            }
        }
     }
}
