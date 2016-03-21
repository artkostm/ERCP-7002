package com.artkostm.core;

import java.io.IOException;

import akka.actor.ActorSystem;
import akka.http.javadsl.model.ContentTypes;
import akka.http.javadsl.server.HttpApp;
import akka.http.javadsl.server.RequestVal;
import akka.http.javadsl.server.Route;
import akka.http.javadsl.server.values.Parameters;

public class AkkaServer extends HttpApp
{
    public static void main(String[] args) throws IOException 
    {
        // boot up server using the route as defined below
        ActorSystem system = ActorSystem.create();
 
        new AkkaServer().bindRoute("127.0.0.1", 8080, system);
        System.out.println("Type RETURN to exit");
        System.in.read();
        system.terminate();
    }

    //ab -n 20000 -c 10 http://localhost:8080/hello
    private RequestVal<String> name = Parameters.stringValue("name").withDefault("Mister X");
    
    @Override
    public Route createRoute()
    {
        Route helloRoute =
                handleWith1(name, (ctx, name) -> ctx.complete("Hello " + name + "!"));
        return
                // here the complete behavior for this server is defined
                route(
                    // only handle GET requests
                    get(
                        // matches the empty path
                        pathSingleSlash().route(
                            // return a constant string with a certain content type
                            complete(ContentTypes.TEXT_HTML_UTF8,
                                    "<html><body>Hello world!</body></html>")
                        ),
                        path("ping").route(
                            // return a simple `text/plain` response
                            complete("PONG!")
                        ),
                        path("hello").route(
                            // uses the route defined above
                            helloRoute
                        )
                    )
                );
    }

}
