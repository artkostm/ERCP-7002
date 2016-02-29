package com.artkostm.core.akka.reaper;

import java.util.concurrent.TimeUnit;

import com.artkostm.core.akka.fsm.PrintAktor;
import com.artkostm.core.akka.util.reaper.ProductionReaper;
import com.artkostm.core.akka.util.reaper.WatchMe;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.dispatch.OnComplete;
import akka.japi.Util;
import akka.pattern.Patterns;
import akka.util.Timeout;
import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;

public class Main 
{
    public static void main(String[] args) throws Exception
    {
        final ActorSystem system = ActorSystem.create("reaper_test_system");
        final ActorRef reaper = system.actorOf(ProductionReaper.props());
        final ActorRef print = system.actorOf(PrintAktor.props());
        
        reaper.tell(new WatchMe(print), ActorRef.noSender());
        
        Future<String> f = Patterns.ask(print, "Hello, World!", Timeout.apply(Duration.create(10, TimeUnit.SECONDS)))
                .mapTo(Util.classTag(String.class));
        f.onComplete(new OnComplete<String>() 
        {
            @Override
            public void onComplete(Throwable arg0, String arg1) throws Throwable 
            {
                System.out.println("\nOnComplete: " + arg1);
                system.stop(print);
            }
        }, system.dispatcher());
        Await.result(f, Duration.Inf());
    }
}
