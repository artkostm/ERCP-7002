package com.artkostm.core.akka.util.reaper;

import akka.actor.ActorRef;
import akka.actor.Terminated;
import akka.actor.UntypedActor;
import scala.collection.mutable.ArrayBuffer;

public abstract class Reaper extends UntypedActor
{
    private final ArrayBuffer<ActorRef> watched = new ArrayBuffer<>();
    
    @Override
    public void onReceive(Object msg) throws Exception 
    {
        if (msg instanceof WatchMe)
        {
            final WatchMe req = (WatchMe) msg;
            context().watch(req.getRef());
            watched.$plus$eq(req.getRef());
        }
        if (msg instanceof Terminated)
        {
            watched.$minus$eq(((Terminated)msg).actor());
            if (watched.isEmpty())
            {
                allSoulsReaped();
            }
        }
    }
    
    protected abstract void allSoulsReaped();
}
