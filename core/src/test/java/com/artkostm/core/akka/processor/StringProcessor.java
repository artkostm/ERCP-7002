package com.artkostm.core.akka.processor;

import com.artkostm.core.akka.Main;
import com.artkostm.core.akka.Worker;
import com.artkostm.core.akka.model.Job;

import akka.actor.Actor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.routing.SmallestMailboxPool;

public class StringProcessor implements Processor<String>
{
    private ActorRef router;
    
    @Override
    public void process(final Actor actor, final String obj) 
    {
        if (router == null)
        {
            router = actor.context().actorOf(new SmallestMailboxPool(100).props(Props.create(Worker.class)), "workers");
        }
        
        final long number = Long.parseLong(obj);
        System.out.println("Number is " + number);
        for (long i = 2; i <= number/2; i++)
        {
            router.tell(new Job(number, i, i+1, Main.counter()), actor.self());
        }
    }
}
