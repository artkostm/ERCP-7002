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

        final ActorRef httpTransformer = system.actorOf(Props.create(HttpTransformer.class));

        final ActorRef httpProducer = system.actorOf(Props.create(HttpProducer.class, httpTransformer));

        final ActorRef httpConsumer = system.actorOf(Props.create(HttpConsumer.class, httpProducer));
    }
}
