package com.artkostm.core.akka.worldcount.server;

import java.util.List;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.japi.pf.ReceiveBuilder;

import com.artkostm.core.akka.worldcount.model.Word;

public class Mapper extends AbstractActor
{
    public Mapper(final ActorRef reducer)
    {
        receive(ReceiveBuilder.match(String.class, msg -> 
        {
            final List<Word> words = TextUtil.evaluateExpression(msg);
            reducer.tell(words, self());
        })
        .matchAny(this::unhandled).build());
    }
}
