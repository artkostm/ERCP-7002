package com.artkostm.core.akka.http.client.common;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.function.BiConsumer;

import com.artkostm.core.akka.extension.ActorSystemAware;

import akka.actor.ActorSystem;
import akka.http.javadsl.ConnectHttp;
import akka.http.javadsl.Http;
import akka.http.javadsl.HttpsConnectionContext;
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
    
    private Flow<HttpRequest, HttpResponse, CompletionStage<OutgoingConnection>> connectionFlow;
    private HttpResponse response;
    
    public HttpEndpoint(final ActorSystem system)
    {
        this.system = system;
        materializer = ActorMaterializer.create(system);
    }
    
    @Override
    public ResponseHandler request(String url)
    {
        return request(HttpRequest.create(url));
    }

    @Override
    public ResponseHandler request(HttpRequest request)
    {
        try
        {
            response = ((CompletableFuture<HttpResponse>) Source.single(request)
                .via(connectionFlow)
                .runWith(Sink.<HttpResponse>head(), materializer)).get();
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
        return this;
    }
    
    @Override
    public CompletionStage<HttpResponse> request(String url, BiConsumer<? super HttpResponse, ? super Throwable> action)
    {
        return request(HttpRequest.create(url), action);
    }

    @Override
    public CompletionStage<HttpResponse> request(HttpRequest request, BiConsumer<? super HttpResponse, ? super Throwable> action)
    {
        final CompletableFuture<HttpResponse> future = (CompletableFuture<HttpResponse>) Source.single(request)
                .via(connectionFlow)
                .runWith(Sink.<HttpResponse>head(), materializer);
        future.whenCompleteAsync(action, system.dispatcher());
        return future;
    };

    @Override
    public HttpRequestRunner createFlow(String host, int port)
    {
        connectionFlow = Http.get(system).outgoingConnection(new ConnectHttp()
        {
            @Override
            public int port()
            {
                return port;
            }
            
            @Override
            public boolean isHttps()
            {
                return false;
            }
            
            @Override
            public String host()
            {
                return host;
            }
            
            @Override
            public Optional<HttpsConnectionContext> connectionContext()
            {
                return null;
            }
        });
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
        return ((CompletableFuture<ByteString>) response.entity().getDataBytes().fold(ByteString.empty(), 
            (z, i) -> 
            {
                return z.concat(i);
            }
        ).runWith(Sink.<ByteString>head(), materializer)).get().utf8String();
    }
}
