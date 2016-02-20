package com.artkostm.core.akka.util.reaper;

import akka.actor.ActorRef;

public class WatchMe
{
    private final ActorRef ref;
    
    public WatchMe(final ActorRef ref) 
    {
        this.ref = ref;
    }

    public ActorRef getRef() 
    {
        return ref;
    }
}
