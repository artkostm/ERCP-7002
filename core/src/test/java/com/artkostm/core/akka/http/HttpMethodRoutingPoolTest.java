package com.artkostm.core.akka.http;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpRequest;

import java.util.concurrent.TimeUnit;

import scala.concurrent.duration.Duration;

import com.artkostm.core.akka.configuration.RouteObject;
import com.artkostm.core.akka.http.message.HttpMessage;
import com.artkostm.core.akka.http.routing.HttpMethodRoutingPool;
import com.artkostm.core.akka.util.reaper.ProductionReaper;
import com.artkostm.core.akka.util.reaper.WatchMe;
import com.artkostm.core.web.controller.converter.Json;
import com.artkostm.core.web.network.router.RouteResult;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.PoisonPill;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.contrib.throttle.Throttler;
import akka.contrib.throttle.TimerBasedThrottler;

public class HttpMethodRoutingPoolTest
{
    public static void main(String[] args) throws InterruptedException
    {
        final ActorSystem system = ActorSystem.create("test");
        final ActorRef httpMethodRoutingPool = system.actorOf(new HttpMethodRoutingPool(30).props(Props.create(HttpMethodTestActor.class)), "pool");
        
        final ActorRef throttler = system.actorOf(Props.create(TimerBasedThrottler.class, new Throttler.Rate(5000, Duration.create(1, TimeUnit.SECONDS))));
        
        final ActorRef reaper = system.actorOf(ProductionReaper.props());
        reaper.tell(new WatchMe(throttler), ActorRef.noSender());
        
        throttler.tell(new Throttler.SetTarget(httpMethodRoutingPool), ActorRef.noSender());
        
        final long start = System.currentTimeMillis();
        
        new Thread(worker(throttler)).start();
        
        new Thread(worker(throttler)).start();
        
        new Thread(worker(throttler)).start();
        
        new Thread(worker(throttler)).start();
        
        new Thread(worker(throttler)).start();
        
        new Thread(worker(throttler)).start();
        
        Thread.sleep(10000);

        System.out.println("Start time is " + start);
        throttler.tell(PoisonPill.getInstance(), ActorRef.noSender());
    }
    
    public static class HttpMethodTestActor extends UntypedActor
    {        
        @Override
        public void onReceive(Object arg0) throws Exception
        {
            System.out.println(Json.toJson(arg0).toString()+ System.currentTimeMillis());
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

        @Override
        public ChannelHandlerContext context()
        {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public void context(ChannelHandlerContext context)
        {
            // TODO Auto-generated method stub
            
        }

        @Override
        public HttpRequest request()
        {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public Object payload()
        {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public void payload(Object obj)
        {
            // TODO Auto-generated method stub
            
        }

        @Override
        public RouteResult<RouteObject> routeResult()
        {
            // TODO Auto-generated method stub
            return null;
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