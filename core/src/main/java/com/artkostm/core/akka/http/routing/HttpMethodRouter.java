package com.artkostm.core.akka.http.routing;

import akka.actor.ActorSystem;
import akka.actor.SupervisorStrategy;
import akka.dispatch.Dispatchers;
import akka.routing.PoolBase;
import akka.routing.Resizer;
import akka.routing.Routee;
import akka.routing.Router;
import scala.Option;
import scala.collection.immutable.IndexedSeq;

public class HttpMethodRouter extends PoolBase
{
    private static final long serialVersionUID = -8668444273017076975L;

    @Override
    public Router createRouter(ActorSystem system) 
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int nrOfInstances(ActorSystem arg0) 
    {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public Option<Resizer> resizer() 
    {
        return Option.apply(new HttpResizer());
    }

    @Override
    public SupervisorStrategy supervisorStrategy() 
    {
        return SupervisorStrategy.defaultStrategy(); //TODO: create custom strategy
    }

    @Override
    public String routerDispatcher() 
    {
        return Dispatchers.DefaultDispatcherId();
    }
    
    public static class HttpResizer implements Resizer
    {
        @Override
        public int resize(IndexedSeq<Routee> routees) 
        {
            return 0;
        }
        
        @Override
        public boolean isTimeForResize(long messageCounter) 
        {
            return false;
        }
    }
}
