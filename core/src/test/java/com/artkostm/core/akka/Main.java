package com.artkostm.core.akka;

import java.util.Date;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;

public class Main 
{
    //5095931000566324969
    public static void main(String[] args) throws InterruptedException
    {
//        final ActorSystem system = ActorSystem.create("testsystem");
//        final ActorRef kernel = system.actorOf(Props.create(Kernel.class), "kernel");
//        final long start = System.currentTimeMillis();
//        kernel.tell("9095932", ActorRef.noSender());
//        Thread.sleep(30000);
//        system.terminate();
//        System.out.println("Time is " + (System.currentTimeMillis() - start) + " ms");
        System.out.println(new Date(1421844317720L));
    }
    
    public static long counter()
    {
        return counter++;
    }
    
    private static long counter = 2;
}