package com.artkostm.core.akka;

import java.util.concurrent.TimeUnit;

import scala.concurrent.Await;
import scala.concurrent.duration.Duration;
import scala.concurrent.duration.FiniteDuration;

import com.artkostm.core.akka.aktors.BinaryEchoAktor;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.japi.Util;
import akka.pattern.Patterns;
import akka.util.Timeout;

public class Main 
{
    //5095931000566324969
    public static void main(String[] args) throws Exception
    {
        final ActorSystem system = ActorSystem.create("testsystem");
//        final ActorRef kernel = system.actorOf(Props.create(Kernel.class), "kernel");
//        final long start = System.currentTimeMillis();
//        final FiniteDuration duration = Duration.create(50, TimeUnit.MINUTES);
//        Await.result(Patterns.ask(kernel, "5095931", new Timeout(duration)), Duration.Inf());
//        System.out.println("Time is " + (System.currentTimeMillis() - start) + " ms");
        
        final ActorRef echo = system.actorOf(Props.create(BinaryEchoAktor.class), "echo");
        final FiniteDuration duration = Duration.create(1, TimeUnit.SECONDS);
        final String binaryResult = Await.result(Patterns.ask(echo, 1032L, new Timeout(duration)).mapTo(Util.classTag(String.class)), duration);
        System.out.println(binaryResult);
        
        Await.ready(system.terminate(), Duration.Inf());
    }
    
    public static long counter()
    {
        return counter++;
    }
    
    private static long counter = 2;
}