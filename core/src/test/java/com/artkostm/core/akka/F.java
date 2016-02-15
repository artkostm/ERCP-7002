package com.artkostm.core.akka;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import scala.concurrent.Await;
import scala.concurrent.ExecutionContextExecutor;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.dispatch.Futures;
import akka.dispatch.OnComplete;
import akka.pattern.Patterns;

public class F extends UntypedActor
{
    private static Random rand = new Random(2010201021430L);
    
    public static void main(String[] args) throws Exception
    {
        final ActorSystem system = system();
        final ExecutionContextExecutor dispatcher = system.dispatcher();
        Future<Long> fr = Futures.future(task, dispatcher);
        Future<Long> sc = Futures.future(task, dispatcher);
        Future<Long> th = Futures.future(task, dispatcher);
        Future<Long> fo = Futures.future(task, dispatcher);
        fr.onComplete(complete, dispatcher);
        sc.onComplete(complete, dispatcher);
        th.onComplete(complete, dispatcher);
        fo.onComplete(complete, dispatcher);
        Future<Iterable<Long>> sec = Futures.sequence(Arrays.asList(fr, sc, th, fo), dispatcher);
        Patterns.pipe(sec, dispatcher).to(system.actorOf(Props.create(F.class))).future().ready(Duration.create(20, TimeUnit.SECONDS), null);
        Await.ready(system.terminate(), Duration.Inf());
    }
    
    private static ActorSystem system()
    {
        return ActorSystem.create("test-system");
    }
    
    private static Callable<Long> task = new Callable<Long>()
    {        
        @Override
        public Long call() throws Exception
        {
            final long edge = rand.nextInt(10000);
            System.err.println("Edge: " + edge);
            for (long i = 0; i <= edge; i++)
            {
                if ( i == edge) return edge;
                Thread.sleep(1);
            }
            return edge;
        }
    };
    
    
    private static final OnComplete<Long> complete = new OnComplete<Long>()
    {
        @Override
        public void onComplete(Throwable arg0, Long arg1) throws Throwable
        {
            System.out.println("Res: " + arg1 + "\nException: " + arg0);
        }
    };

    @Override
    public void onReceive(Object arg0) throws Exception
    {
        if (arg0 instanceof Iterable)
        {
            System.out.println("Message: " + arg0);
        }
        else
        {
            unhandled(arg0);
        }
    }
}
