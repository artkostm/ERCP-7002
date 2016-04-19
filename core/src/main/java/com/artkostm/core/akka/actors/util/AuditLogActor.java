package com.artkostm.core.akka.actors.util;

import java.util.Date;

import akka.actor.Props;
import akka.persistence.DeleteMessagesFailure;
import akka.persistence.DeleteMessagesSuccess;
import akka.persistence.UntypedPersistentActor;

public class AuditLogActor extends UntypedPersistentActor
{
    @Override
    public String persistenceId()
    {
        return self().path().name();
    }

    @Override
    public void onReceiveCommand(Object msg) throws Exception
    {
        if (msg instanceof DeleteMessagesSuccess)
        {
            return;
        }
        if (msg instanceof DeleteMessagesFailure)
        {
            System.err.println(((DeleteMessagesFailure) msg).cause());
        }
        persistAsync(String.format("event-monitor[%s]:{%s}", new Date().toString(), msg), event -> {});
    }

    @Override
    public void onReceiveRecover(Object msg) throws Exception
    {
        //System.out.println("OnReceiveRecover: " + msg);
        //deleteMessages(lastSequenceNr());
    }
    
    public static Props props()
    {
        return Props.create(AuditLogActor.class);
    }
}
