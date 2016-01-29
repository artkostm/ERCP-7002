package com.artkostm.core.akka.worldcount.client;

import akka.actor.AbstractActor;
import akka.actor.ActorSelection;
import akka.japi.pf.ReceiveBuilder;

public class ClientActor extends AbstractActor
{
    private long start;
    
    public ClientActor(final ActorSelection server)
    {
        receive(ReceiveBuilder.match(String.class, msg -> 
        {
            if ("EOF".equals(msg))
            {
                server.tell("DISPLAY_LIST", self());
            }
            else
            {
                server.tell(msg, self());
            }
        }).build());
    }
    
    
    @Override
    public void preStart() throws Exception
    {
        start = System.currentTimeMillis();
    }
    
    @Override
    public void postStop() throws Exception
    {
        final long timeSpent = (System.currentTimeMillis() - start) / 1000;
        System.out.println(
            String.format("\n\tClientActor estimate: \t\t\n\tCalculation time: \t%s Secs", timeSpent));
    }
}
