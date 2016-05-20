package com.artkostm.core;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

import akka.actor.ActorSystem;
import akka.http.javadsl.model.ContentTypes;
import akka.http.javadsl.server.HttpApp;
import akka.http.javadsl.server.RequestVal;
import akka.http.javadsl.server.Route;
import akka.http.javadsl.server.values.Parameters;
import akka.stream.javadsl.Sink;
import akka.util.ByteString;

public class AkkaServer extends HttpApp
{
    //ab -n 20000 -c 10 http://localhost:8060/hello
    private RequestVal<String> name = Parameters.stringValue("name").withDefault("Mister X");
    
    @Override
    public Route createRoute()
    {
        Route helloRoute = handleWith1(name, 
            (ctx, name) ->
                { 
                    final CompletableFuture<ByteString> f = (CompletableFuture<ByteString>) 
                            ctx.request().entity().getDataBytes().fold(ByteString.empty(), 
                                (z, i) -> z.concat(i)).runWith(Sink.<ByteString>head(), ctx.materializer());
                    return ctx.complete("Hello " + name + "!" + "\n" + f.get().utf8String()); 
                });
        return route(
            post(pathSingleSlash().route(
                complete(ContentTypes.TEXT_HTML_UTF8,"<html><body>Hello world!</body></html>")
                ),
                path("ping").route(
                    complete("PONG!")
                    ),
                path("hello").route(
                    helloRoute
                    )
                )
            );
    }
    
    public static void main(String[] args) throws IOException 
    {
        final ActorSystem system = ActorSystem.create();
 
        new AkkaServer().bindRoute("127.0.0.1", 8060, system);
        System.out.println("Type RETURN to exit");
        System.in.read();
        system.terminate();
    }

}
