package com.artkostm.core.akka.worldcount.server;

import java.util.Iterator;
import java.util.List;
import java.util.NavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

import com.artkostm.core.akka.worldcount.model.Word;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.japi.pf.ReceiveBuilder;

public class Reducer extends AbstractActor
{
    @SuppressWarnings("unchecked")
    public Reducer(final ActorRef aggregator)
    {
        receive(ReceiveBuilder.match(List.class, list -> aggregator.tell(reduce(list), self()))
            .matchAny(this::unhandled).build());
    }
    
    private NavigableMap<String, Integer> reduce(List<Word> list) 
    {
        final NavigableMap<String, Integer> reducedMap = new ConcurrentSkipListMap<String, Integer>();

        final Iterator<Word> iter = list.iterator();
        while (iter.hasNext()) 
        {
            final Word result = iter.next();
            if (reducedMap.containsKey(result.getWord())) 
            {
                Integer value = reducedMap.get(result.getWord());
                value++;
                reducedMap.put(result.getWord(), value);
            } 
            else 
            {
                reducedMap.put(result.getWord(), Integer.valueOf(1));
            }
        }
        return reducedMap;
    }
}
