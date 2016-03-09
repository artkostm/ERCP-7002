package com.artkostm.core.akka.http.router;

import com.artkostm.core.akka.http.message.HttpMessage;

import akka.actor.ActorSystem;
import akka.dispatch.Dispatchers;
import akka.routing.GroupBase;
import akka.routing.Router;

public class HttpMethodRouter extends GroupBase
{
    private static final long serialVersionUID = 1L;
    
    //GET - balancing pool
    //POST - balancing pool
    //PUT - balancing pool
    //DELETE - balancing pool
    //Other - smallest mailboxes
    
    public HttpMethodRouter()
    {
        
    }

    @Override
    public String routerDispatcher()
    {
        return Dispatchers.DefaultDispatcherId();
    }
    
    @Override
    public boolean isManagementMessage(Object msg)
    {
        if (msg instanceof HttpMessage)
        {
            return true;
        }
        return false;
    }
    
    

    @Override
    public Router createRouter(ActorSystem system)
    {
        
        return new Router(new HttpRoutingLogic());
    }

    @Override
    public Iterable<String> getPaths(ActorSystem system)
    {
        
        return null;
    }
}
