package com.artkostm.core.akka.http;

import java.util.concurrent.TimeUnit;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.contrib.throttle.Throttler;
import akka.contrib.throttle.TimerBasedThrottler;
import scala.concurrent.duration.Duration;

public class ThrottlerTest 
{
    public static void main(String [] args) throws InterruptedException
    {
        final ActorSystem system = ActorSystem.create();
        final ActorRef throttler = system.actorOf(Props.create(TimerBasedThrottler.class, new Throttler.Rate(6, Duration.create(1, TimeUnit.SECONDS))));
        final ActorRef printer = system.actorOf(Props.create(PrintEndpoint.class));
        
        throttler.tell(new Throttler.SetTarget(printer), ActorRef.noSender());
        
        throttler.tell("Hello1", ActorRef.noSender());
        throttler.tell("Hello2", ActorRef.noSender());
        throttler.tell("Hello3", ActorRef.noSender());
        throttler.tell("Hello4", ActorRef.noSender());
        throttler.tell("Hello5", ActorRef.noSender());
        throttler.tell("Hello6", ActorRef.noSender());
        throttler.tell("Hello7", ActorRef.noSender());
        throttler.tell("Hello8", ActorRef.noSender());
        throttler.tell("Hello9", ActorRef.noSender());
        throttler.tell("Hello10", ActorRef.noSender());
        
        Thread.sleep(5000);
        system.terminate();
    }
    
    public static class PrintEndpoint extends UntypedActor
    {
        @Override
        public void onReceive(Object msg) throws Exception 
        {
            System.out.println("Printer: [" + msg + "]");
        }
    }
}
