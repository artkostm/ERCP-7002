package com.artkostm.core.akka.extension;

import akka.actor.ActorSystem;

public interface ActorSystemAware
{
    void setActorSystem(final ActorSystem system);
    
    ActorSystem getActorSystem();
}
