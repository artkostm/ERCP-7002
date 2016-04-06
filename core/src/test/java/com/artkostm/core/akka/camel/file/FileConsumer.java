package com.artkostm.core.akka.camel.file;

import akka.actor.ActorRef;
import akka.camel.CamelMessage;
import akka.camel.javaapi.UntypedConsumerActor;

public class FileConsumer extends UntypedConsumerActor
{
    final ActorRef producer;
    
    public FileConsumer(final ActorRef producer)
    {
        this.producer = producer;
    }
    
    @Override
    public String getEndpointUri()
    {
        return "file:D:\\?fileName=transactions.csv&noop=true";
    }

    @Override
    public void onReceive(Object message) throws Exception
    {
        if (message instanceof CamelMessage) 
        {
            producer.forward(message, context());
        } 
        else unhandled(message);
        
    }
}
