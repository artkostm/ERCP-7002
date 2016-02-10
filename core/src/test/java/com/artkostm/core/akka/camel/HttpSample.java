package com.artkostm.core.akka.camel;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;

public class HttpSample
{
    @SuppressWarnings("unused")
    public static void main(String[] args)
    {
        final ActorSystem system = ActorSystem.create("camel-system");
        final ActorRef httpConsumer = system.actorOf(Props.create(HttpConsumer.class));
    }
}
