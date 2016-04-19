package com.artkostm.core.akka.actors.util;

import akka.actor.DeadLetter;
import akka.actor.Props;
import akka.actor.UntypedActor;

public class ConsoleLogActor extends UntypedActor
{
    @Override
    public void onReceive(Object msg) throws Exception
    {
        if (msg instanceof DeadLetter)
        {
            System.err.println("[INFO] deadLetter object: " + ((DeadLetter) msg).message());
        }
    }
    
    public static Props props()
    {
        return Props.create(ConsoleLogActor.class);
    }
}
