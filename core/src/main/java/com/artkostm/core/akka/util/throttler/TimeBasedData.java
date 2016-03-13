package com.artkostm.core.akka.util.throttler;

import akka.actor.ActorRef;

public class TimeBasedData implements Data
{
    private ActorRef target;
    private int vouchersLeft;
    private scala.collection.mutable.Queue<Object> queue;
    
    public TimeBasedData(ActorRef target, int vouchersLeft, scala.collection.mutable.Queue<Object> queue) 
    {
        this.target = target;
        this.vouchersLeft = vouchersLeft;
        this.queue = queue;
    }
    
    public TimeBasedData(ActorRef target, int vouchersLeft) 
    {
        this.target = target;
        this.vouchersLeft = vouchersLeft;
        this.queue = new scala.collection.mutable.Queue<>();
    }

    public ActorRef getTarget() 
    {
        return target;
    }

    public int getVouchersLeft() 
    {
        return vouchersLeft;
    }

    public scala.collection.mutable.Queue<Object> getQueue() 
    {
        return queue;
    }

    public void setTarget(ActorRef target) 
    {
        this.target = target;
    }

    public void setVouchersLeft(int vouchersLeft) 
    {
        this.vouchersLeft = vouchersLeft;
    }

    public void setQueue(scala.collection.mutable.Queue<Object> queue) 
    {
        this.queue = queue;
    }
}
