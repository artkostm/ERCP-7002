package com.artkostm.core.akka.processor;

import akka.actor.Actor;

public interface Processor<T>
{
    void process(final Actor actor, final T obj);
}
