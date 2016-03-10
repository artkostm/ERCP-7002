package com.artkostm.core.akka.http;

import com.artkostm.core.akka.http.message.HttpMessage;

import akka.actor.UntypedActor;

public abstract class ActorController extends UntypedActor
{
    @Override
    public void onReceive(Object msg) throws Exception 
    {
        if (msg instanceof HttpMessage)
        {
            onReceive((HttpMessage)msg);
        }
        else
        {
            unhandled(msg);
        }
    }
    
    abstract void onReceive(HttpMessage msg) throws Exception;
}
