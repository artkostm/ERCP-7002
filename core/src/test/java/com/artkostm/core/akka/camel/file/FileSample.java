package com.artkostm.core.akka.camel.file;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.camel.Camel;
import akka.camel.CamelExtension;

public class FileSample
{
    public static void main(String[] args) throws Exception
    {
        final ActorSystem system = ActorSystem.create("camel-system");
        final Camel camel = CamelExtension.get(system);
        camel.context().start();
        final ActorRef producer = system.actorOf(Props.create(FileProducer.class), "file-producer");
        system.actorOf(Props.create(FileConsumer.class, producer), "file-consumer");
    }
}
