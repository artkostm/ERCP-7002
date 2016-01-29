package com.artkostm.core.akka;

import java.util.concurrent.Callable;

import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.dispatch.Futures;
import akka.pattern.Patterns;

public class Main 
{
    //5095931000566324969
    public static void main(String[] args) throws Exception
    {
        final ActorSystem system = ActorSystem.create("testsystem");
        
        final ActorRef kernel = system.actorOf(Props.create(Kernel.class), "kernel");
        
        final Future<String> start = Futures.future(new Callable<String>()
        {
            @Override
            public String call() throws Exception
            {
                System.out.println(Thread.currentThread().getName());
                return "509593100";
            }
         }, system.dispatcher());
        
        Patterns.pipe(start, system.dispatcher()).to(kernel);

        Thread.sleep(10000);
        System.out.println(Thread.currentThread().getName());
        
        Await.ready(system.terminate(), Duration.Inf());
    }
    
    public static long counter()
    {
        return counter++;
    }
    
    private static long counter = 2;
}