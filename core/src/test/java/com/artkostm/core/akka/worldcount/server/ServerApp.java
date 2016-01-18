package com.artkostm.core.akka.worldcount.server;

import com.typesafe.config.ConfigFactory;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.routing.RoundRobinPool;

public class ServerApp
{
    @SuppressWarnings("unused")
    public ServerApp(final int nr_of_rworkers, final int nr_of_mworkers)
    {
        
        final ActorSystem system = ActorSystem.create("ServerApp", ConfigFactory.load().getConfig("WCMapReduceApp"));
        final ActorRef aggregator = system.actorOf(Props.create(Aggregator.class));
        final ActorRef reducer = system.actorOf(new RoundRobinPool(nr_of_rworkers).props(Props.create(Reducer.class, aggregator)));
        final ActorRef mapper = system.actorOf(new RoundRobinPool(nr_of_mworkers).props(Props.create(Mapper.class, reducer)));
        final ActorRef mediator = system.actorOf(Props.create(ReduceMediator.class, aggregator, mapper)
            .withDispatcher("priorityMailBox-dispatcher"), "WCMapReduceActor");
    }
    
    public static void main(String[] args)
    {
        new ServerApp(5, 5);
    }
}
