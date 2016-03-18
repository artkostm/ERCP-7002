package com.artkostm.core.akka.typesafe;

import com.artkostm.core.akka.RouterFactory;
import com.artkostm.core.configuration.ConfigAggregator;

public class Main
{
    public static void main(String[] args) throws Exception
    {
        final RouterFactory factory = new RouterFactory();
        System.out.println(factory.get(ConfigAggregator.load("/application3.conf").configuration()));
    }
}
