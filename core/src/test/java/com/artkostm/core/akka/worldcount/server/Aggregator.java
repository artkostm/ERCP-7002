package com.artkostm.core.akka.worldcount.server;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import akka.actor.AbstractActor;
import akka.japi.pf.ReceiveBuilder;

public class Aggregator extends AbstractActor
{
    private Map<String, Integer> finalReducedMap = new HashMap<String, Integer>();
    
    public Aggregator()
    {
        receive(ReceiveBuilder.match(Map.class, this::aggregateInMemoryReduce)
            .match(String.class, msg -> 
            {
                if (msg.compareTo("DISPLAY_LIST") == 0) 
                {
                    //getSender().tell(finalReducedMap.toString());
                    System.out.println(finalReducedMap.toString());
                }
            }).build());
    }
    
    private void aggregateInMemoryReduce(final Map<String, Integer> reducedList) 
    {

        final Iterator<String> iter = reducedList.keySet().iterator();
        while (iter.hasNext()) 
        {
            final String key = iter.next();
            if (finalReducedMap.containsKey(key)) 
            {
                final Integer count = reducedList.get(key) + finalReducedMap.get(key);
                finalReducedMap.put(key, count);
            } 
            else 
            {
                finalReducedMap.put(key, reducedList.get(key));
            }
        }
    }
}
