package com.artkostm.core.akka.processor;

import akka.actor.Actor;

public class DefaultProcessor implements Processor<Object>
{
    @Override
    public void process(final Actor actor, Object obj) 
    {
        System.out.println("DEFAULT");
        actor.unhandled(obj);
    }
}
