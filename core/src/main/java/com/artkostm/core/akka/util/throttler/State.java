package com.artkostm.core.akka.util.throttler;

public interface State
{
    public static State Idle = new State()
    {};
    
    public static State Active = new State()
    {};
}
