package com.artkostm.core.akka.extension;

import akka.actor.ActorSystem;

public interface ActorSystemAware
{
    void actorSystem(final ActorSystem system);
    
    ActorSystem actorSystem();
}
