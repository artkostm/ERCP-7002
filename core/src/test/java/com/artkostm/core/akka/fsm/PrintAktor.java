package com.artkostm.core.akka.fsm;

import akka.actor.ActorRef;
import akka.actor.PoisonPill;
import akka.actor.Props;
import akka.actor.UntypedActor;

public class PrintAktor extends UntypedActor
{
    @Override
    public void onReceive(Object arg0) throws Exception
    {
        sender().tell(String.valueOf(arg0), self());
        self().tell(PoisonPill.getInstance(), ActorRef.noSender());
    }
    
    public static Props props()
    {
        return Props.create(PrintAktor.class);
    }
}
