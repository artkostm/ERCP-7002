package com.artkostm.core.akka.http;

import com.artkostm.core.akka.http.message.HttpMessage;
import com.artkostm.core.akka.http.routing.HttpMethodRoutingPool;
import com.artkostm.core.akka.util.reaper.ProductionReaper;
import com.artkostm.core.akka.util.reaper.WatchMe;
import com.artkostm.core.web.controller.converter.Json;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.PoisonPill;
import akka.actor.Props;
import akka.actor.UntypedActor;

public class HttpMethodRoutingPoolTest
{
    public static void main(String[] args) throws InterruptedException
    {
        final ActorSystem system = ActorSystem.create("test");
        final ActorRef httpMethodRoutingPool = system.actorOf(new HttpMethodRoutingPool(30).props(Props.create(HttpMethodTestActor.class)), "pool");
        
        final ActorRef reaper = system.actorOf(ProductionReaper.props());
        reaper.tell(new WatchMe(httpMethodRoutingPool), ActorRef.noSender());
        final long start = System.currentTimeMillis();
        
        new Thread(worker(httpMethodRoutingPool)).start();
        
        new Thread(worker(httpMethodRoutingPool)).start();
        
        new Thread(worker(httpMethodRoutingPool)).start();
        
        new Thread(worker(httpMethodRoutingPool)).start();
        
        new Thread(worker(httpMethodRoutingPool)).start();
        
        
//        system.eventStream().subscribe(httpMethodRoutingPool, DeadLetter.class);
        
        Thread.sleep(10000);
        System.out.println("Start time is " + start);
//        system.deadLetters().tell(new HttpMessageImpl(HttpMethods.DELETE), ActorRef.noSender());
        httpMethodRoutingPool.tell(PoisonPill.getInstance(), ActorRef.noSender());
    }
    
    public static class HttpMethodTestActor extends UntypedActor
    {        
        @Override
        public void onReceive(Object arg0) throws Exception
        {
            System.out.println(Json.toJson(arg0).toString() + System.currentTimeMillis());
        }
    }
    
    public static class HttpMessageImpl implements HttpMessage
    { 
        private final HttpMethods method;
        
        private final String payload;
        
        public HttpMessageImpl(HttpMethods method, String payload)
        {
            this.method = method;
            this.payload = payload;
        }
        
        @Override
        public HttpMethods method()
        {
            return method;
        }

        public HttpMethods getMethod()
        {
            return method;
        }
        
        public String getPayload()
        {
            return payload;
        }

        @Override
        public String toString()
        {
            return "HttpMessage [method=" + method + "]:"+System.currentTimeMillis();
        }
    }
    
    private static Runnable worker(final ActorRef pool)
    {
        return new Runnable()
        { 
            @Override
            public void run()
            {
                for (int i = 0; i < 7000; i++)
                {
                    pool.tell(new HttpMessageImpl(HttpMethods.PUT, "Hello, World!"), ActorRef.noSender());
                }
            }
        };
    }
}
