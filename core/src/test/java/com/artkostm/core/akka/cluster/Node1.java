package com.artkostm.core.akka.cluster;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import akka.actor.ActorSystem;
import akka.actor.Props;

public class Node1
{
    public static void main(String[] args)
    {
        String[] ports = {"2551"};
        for (String port : ports) 
        {
            final Config config = ConfigFactory.parseString(
                "akka.remote.netty.tcp.port=" + port).withFallback(
                ConfigFactory.load().getConfig("ClusterExample"));

            final ActorSystem system = ActorSystem.create("my_cluster", config);
            system.actorOf(Props.create(ClusterListener.class), "clusterListener");
        }
    }
}
