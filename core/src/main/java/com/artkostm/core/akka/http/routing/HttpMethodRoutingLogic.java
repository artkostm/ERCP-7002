package com.artkostm.core.akka.http.routing;

import akka.routing.Routee;
import akka.routing.RoutingLogic;
import scala.collection.immutable.IndexedSeq;

public class HttpMethodRoutingLogic implements RoutingLogic
{

    @Override
    public Routee select(Object msg, IndexedSeq<Routee> routees) 
    {
        
        return null;
    }

}
