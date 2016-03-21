package com.artkostm.core.akka.typesafe;

import akka.actor.ActorSystem;

import com.artkostm.core.akka.configuration.RouterFactory;
import com.artkostm.core.configuration.ConfigAggregator;

public class Main
{
    public static void main(String[] args) throws Exception
    {
        final ActorSystem system = ActorSystem.create();
        final RouterFactory factory = new RouterFactory(system);
        System.out.println(factory.get(ConfigAggregator.load("/application3.conf").configuration()));
    }
}
