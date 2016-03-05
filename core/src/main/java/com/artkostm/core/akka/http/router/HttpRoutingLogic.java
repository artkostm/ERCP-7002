package com.artkostm.core.akka.http.router;

import scala.collection.immutable.IndexedSeq;
import akka.routing.BalancingRoutingLogic;
import akka.routing.Routee;
import akka.routing.RoutingLogic;
import akka.routing.SmallestMailboxRoutingLogic;

public class HttpRoutingLogic implements RoutingLogic
{
    private final BalancingRoutingLogic balancingRoutingLogic;
    private final SmallestMailboxRoutingLogic smallestMailboxRoutingLogic;
    
    public HttpRoutingLogic()
    {
        this.balancingRoutingLogic = new BalancingRoutingLogic();
        this.smallestMailboxRoutingLogic = new SmallestMailboxRoutingLogic();
    }



    @Override
    public Routee select(Object msg, IndexedSeq<Routee> routees)
    {
        
        return null;
    }
}
