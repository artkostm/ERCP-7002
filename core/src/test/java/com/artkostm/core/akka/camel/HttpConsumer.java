package com.artkostm.core.akka.camel;

import akka.actor.ActorRef;
import akka.camel.CamelMessage;
import akka.camel.javaapi.UntypedConsumerActor;

public class HttpConsumer extends UntypedConsumerActor
{
    private ActorRef producer;

    public HttpConsumer()
    {
    }

    public String getEndpointUri()
    {
        return "jetty:http://0.0.0.0:8875/";
    }

    public void onReceive(final Object message)
    {
        CamelMessage msg = (CamelMessage) message;
        final String uri = (String) msg.getHeaders().get("my-custom-uri");
        final ActorRef transformer = context().system().actorOf(HttpTransformer.props());
        producer = context().system().actorOf(HttpProducer.props(transformer, uri));
        producer.forward(message, context());
    }
}
