package com.artkostm.core.akka.http;

import com.artkostm.core.akka.http.message.HttpMessage;
import com.artkostm.core.akka.http.routing.HttpMethodRoutingPool;
import com.artkostm.core.akka.util.reaper.ProductionReaper;
import com.artkostm.core.akka.util.reaper.WatchMe;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.DeadLetter;
import akka.actor.PoisonPill;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class HttpMethodRoutingPoolTest
{
    public static void main(String[] args) throws InterruptedException
    {
        final ActorSystem system = ActorSystem.create("test");
        final ActorRef httpMethodRoutingPool = system.actorOf(new HttpMethodRoutingPool(15).props(Props.create(HttpMethodTestActor.class)));
        
        final ActorRef reaper = system.actorOf(ProductionReaper.props());
        reaper.tell(new WatchMe(httpMethodRoutingPool), ActorRef.noSender());
        
        httpMethodRoutingPool.tell(new HttpMessageImpl(HttpMethods.CONNECT), ActorRef.noSender());
        
        system.actorSelection("akka://test/user/$a/$a").tell(new HttpMessageImpl(HttpMethods.OPTIONS), ActorRef.noSender());
        for (int i = 0; i < 100; i++)
        {
            httpMethodRoutingPool.tell(new HttpMessageImpl(i % 2 == 0 ? HttpMethods.GET : HttpMethods.POST), ActorRef.noSender());
        }
        
        system.eventStream().subscribe(httpMethodRoutingPool, DeadLetter.class);
        
        Thread.sleep(2000);
        system.deadLetters().tell(new HttpMessageImpl(HttpMethods.DELETE), ActorRef.noSender());
        httpMethodRoutingPool.tell(PoisonPill.getInstance(), ActorRef.noSender());
    }
    
    public static class HttpMethodTestActor extends UntypedActor
    {
        LoggingAdapter log = Logging.getLogger(getContext().system(), this);
        
        @Override
        public void preStart() 
        {
          log.info("Starting");
        }
        
        @Override
        public void onReceive(Object arg0) throws Exception
        {
            System.out.println(this.self().path() + " actor obtain " + arg0);
            if (arg0 instanceof HttpMessage)
            {
                if (((HttpMessage) arg0).method() == HttpMethods.GET)
                {
                    throw new RuntimeException("Hello");
                }
            }
        }
    }
    
    public static class HttpMessageImpl implements HttpMessage
    { 
        private final HttpMethods method;
        
        public HttpMessageImpl(HttpMethods method)
        {
            this.method = method;
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

        @Override
        public String toString()
        {
            return "HttpMessage [method=" + method + "]";
        }
    }
}
