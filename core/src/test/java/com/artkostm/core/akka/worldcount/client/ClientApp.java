package com.artkostm.core.akka.worldcount.client;

import com.typesafe.config.ConfigFactory;

import akka.actor.ActorRef;
import akka.actor.ActorSelection;
import akka.actor.ActorSystem;
import akka.actor.Props;

public class ClientApp
{
    public static void main(String[] args) throws InterruptedException
    {
        final String filename = "testtext.txt";
        
        final ActorSystem system = ActorSystem.create("ClientApp",
            ConfigFactory.load().getConfig("WordCountApp"));
        
        final ActorRef fileReader = system.actorOf(Props.create(FileReadActor.class));
        
        final ActorSelection remoteActor = system.actorSelection("akka.tcp://ServerApp@127.0.0.1:2554/user/WCMapReduceActor");
        
        final ActorRef client = system.actorOf(Props.create(ClientActor.class, remoteActor));
        
        fileReader.tell(filename, client);
        
        //system.terminate();//TODO:
    }
}
