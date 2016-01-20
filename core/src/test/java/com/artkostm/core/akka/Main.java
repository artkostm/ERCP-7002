package com.artkostm.core.akka;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;
import scala.concurrent.duration.FiniteDuration;

import com.artkostm.core.akka.aktors.BinaryEchoAktor;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.dispatch.Futures;
import akka.dispatch.OnComplete;
import akka.dispatch.OnSuccess;
import akka.japi.Util;
import akka.pattern.Patterns;
import akka.util.Timeout;

public class Main 
{
    //5095931000566324969
    public static void main(String[] args) throws Exception
    {
        System.setProperty("name", "Artsiom");
        final ActorSystem system = ActorSystem.create("testsystem");
        
        final ActorRef echo = system.actorOf(Props.create(BinaryEchoAktor.class), "echo");
        final FiniteDuration duration = Duration.create(1, TimeUnit.SECONDS);
        final String binaryResult = Await.result(Patterns.ask(echo, 1032L, new Timeout(duration)).mapTo(Util.classTag(String.class)), Duration.Inf());
        System.out.println(binaryResult);
        
        Future<String> f = Futures.future(new Callable<String>()
        {
            @Override
            public String call() throws Exception
            {
                return "1383272";
            }
        }, system.dispatcher());
        
        f.onSuccess(s, system.dispatcher());
        
        f.onComplete(c, system.dispatcher());
        
        Patterns.pipe(f, system.dispatcher()).to(echo);
        Await.ready(system.terminate(), Duration.Inf());
    }
    
    private static OnSuccess<String> s = new OnSuccess<String>()
    {
        @Override
        public void onSuccess(String arg0) throws Throwable
        {
            System.out.println("Success: " + arg0);
        }
    };
    
    private static OnComplete<String> c = new OnComplete<String>()
    {
        @Override
        public void onComplete(Throwable arg0, String arg1) throws Throwable
        {
            System.out.println("Complete: " + arg1);
        }
    };
    
    public static long counter()
    {
        return counter++;
    }
    
    private static long counter = 2;
}