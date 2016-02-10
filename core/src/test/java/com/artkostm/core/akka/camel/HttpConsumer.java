package com.artkostm.core.akka.camel;

import akka.actor.ActorRef;
import akka.camel.javaapi.UntypedConsumerActor;

public class HttpConsumer extends UntypedConsumerActor
{
    private ActorRef producer;

    public HttpConsumer(final ActorRef producer)
    {
        this.producer = producer;
    }

    public String getEndpointUri()
    {
        return "jetty:http://0.0.0.0:8875/";
    }

    public void onReceive(final Object message)
    {
        producer.forward(message, getContext());
    }
}
