package com.artkostm.core.akka.configuration;

import akka.actor.ActorRef;

public class RouteObject
{
    private final Class<?> clazz;
    private final ActorRef actor;
    private final int spinCount;
    
    public RouteObject(final Class<?> clazz, final ActorRef actor)
    {
        this.clazz = clazz;
        this.actor = actor;
        spinCount = 1;
    }
    
    public RouteObject(final Class<?> clazz, final ActorRef actor, final int spinCount)
    {
        this.clazz = clazz;
        this.actor = actor;
        this.spinCount = spinCount;
    }

    public Class<?> getClazz()
    {
        return clazz;
    }

    public int getSpinCount()
    {
        return spinCount;
    }

    public ActorRef getActor()
    {
        return actor;
    }

    @Override
    public String toString()
    {
        return "RouteObject [clazz=" + clazz + ", actor=" + actor + ", spinCount=" + spinCount + "]";
    }
}
