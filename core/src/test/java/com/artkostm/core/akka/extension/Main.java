package com.artkostm.core.akka.extension;

import static com.artkostm.core.akka.extension.netty.NettyProvider.Netty;
import com.typesafe.config.ConfigFactory;

import akka.actor.ActorSystem;

public class Main
{
    public static void main(String[] args)
    {
        final ActorSystem system = ActorSystem.create("extension", ConfigFactory.load());
        System.out.println(Netty.get(system).config());
    }
}
