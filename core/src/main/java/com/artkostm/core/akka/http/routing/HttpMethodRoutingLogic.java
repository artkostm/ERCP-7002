package com.artkostm.core.akka.http.routing;

import akka.routing.BalancingRoutingLogic;
import akka.routing.RoundRobinRoutingLogic;
import akka.routing.Routee;
import akka.routing.RoutingLogic;
import akka.routing.SmallestMailboxRoutingLogic;
import scala.collection.immutable.IndexedSeq;

public class HttpMethodRoutingLogic implements RoutingLogic
{
    private final BalancingRoutingLogic balancingLogic;
    private final SmallestMailboxRoutingLogic smallestMailboxLogic;
    private final RoundRobinRoutingLogic roundRobinLogic;
    
    public HttpMethodRoutingLogic() 
    {
        balancingLogic = new BalancingRoutingLogic();
        smallestMailboxLogic = new SmallestMailboxRoutingLogic();
        roundRobinLogic = new RoundRobinRoutingLogic();
    }
    
    @Override
    public Routee select(Object msg, IndexedSeq<Routee> routees) 
    {
        if (msg instanceof HttpMessage)
        {
            final HttpMessage message = (HttpMessage) msg;
            if (message.method() == HttpMethods.POST || 
                    message.method() == HttpMethods.GET || 
                    message.method() == HttpMethods.PUT)
            {
                return balancingLogic.select(message, routees);
            }
            else
            {
                return smallestMailboxLogic.select(message, routees);
            }
            
        }
        else 
        {
            return roundRobinLogic.select(msg, routees);
        }
    }

}
