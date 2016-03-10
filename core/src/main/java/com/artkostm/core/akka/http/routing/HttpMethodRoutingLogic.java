package com.artkostm.core.akka.http.routing;

//import com.artkostm.core.akka.http.HttpMethods;
import com.artkostm.core.akka.http.message.HttpMessage;

//import akka.routing.BalancingRoutingLogic;
import akka.routing.Routee;
import akka.routing.RoutingLogic;
import akka.routing.SmallestMailboxRoutingLogic;
import scala.collection.immutable.IndexedSeq;

public class HttpMethodRoutingLogic implements RoutingLogic
{
//    private final BalancingRoutingLogic balancingLogic;
    private final SmallestMailboxRoutingLogic smallestMailboxLogic;
    
    public HttpMethodRoutingLogic() 
    {
//        balancingLogic = new BalancingRoutingLogic();
        smallestMailboxLogic = new SmallestMailboxRoutingLogic();
    }
    
    @Override
    public Routee select(Object msg, IndexedSeq<Routee> routees) 
    {        
        if (msg instanceof HttpMessage)
        {
            final HttpMessage message = (HttpMessage) msg;
            return smallestMailboxLogic.select(message, routees);
        }
        else
        {
            return smallestMailboxLogic.select(msg, routees);
        }
    }

}
