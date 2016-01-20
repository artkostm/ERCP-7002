package com.artkostm.core.akka.worldcount.server;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.japi.pf.ReceiveBuilder;

public class ReduceMediator extends AbstractActor
{
    public ReduceMediator(final ActorRef aggregator, final ActorRef mapRouter)
    {
        receive(ReceiveBuilder.match(String.class, msg ->
        {
            if ("DISPLAY_LIST".equals(msg))
            {
                aggregator.tell(msg, self());
            }
            else 
            {
                mapRouter.tell(msg, self());
            }
        })
        .matchAny(this::unhandled).build());
    }
}
