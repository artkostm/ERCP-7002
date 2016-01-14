package com.artkostm.core.akka.aktors;

import akka.actor.AbstractActor;
import akka.japi.pf.ReceiveBuilder;

public class BinaryEchoAktor extends AbstractActor
{
    public BinaryEchoAktor()
    {
        receive(
            ReceiveBuilder.match(String.class, System.out::println)
                .match(Long.class, this::process)
                .match(Integer.class, this::process)
                .build()
        );
    }
    
    protected void process(Long number)
    {
        sender().tell(Long.toBinaryString(number), self());
    }
    
    protected void process(Integer number)
    {
        sender().tell(Integer.toBinaryString(number), self());
    }
}
