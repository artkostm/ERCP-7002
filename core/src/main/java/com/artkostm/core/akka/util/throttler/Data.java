package com.artkostm.core.akka.util.throttler;

import akka.actor.ActorRef;
import scala.concurrent.duration.Duration;

public interface Data
{
    public static Data None = new Data()
    {};
    
    public static Data Tick = new Data()
    {};
    
    public static class Rate implements Data
    {
        private final int nrOfCalls;
        private final Duration duration;
        
        public Rate(int nrOfCalls, Duration duration)
        {
            this.nrOfCalls = nrOfCalls;
            this.duration = duration;
        }
        
        public long durationInMillis()
        {
            return duration.toMillis();
        }

        public int getNrOfCalls()
        {
            return nrOfCalls;
        }

        public Duration getDuration()
        {
            return duration;
        }
    }
    
    public static class Queue implements Data
    {
        private final Object msg;
        
        public Queue(Object msg)
        {
            this.msg = msg;
        }
        
        public Object getMsg()
        {
            return msg;
        }
    }
    
    public static class SetTarget implements Data
    {
        private final ActorRef target;
        
        public SetTarget(ActorRef target)
        {
            this.target = target;
        }
        
        public ActorRef getTarget()
        {
            return target;
        }
    }
}
