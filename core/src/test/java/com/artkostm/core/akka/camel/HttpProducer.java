package com.artkostm.core.akka.camel;

import java.util.HashSet;
import java.util.Set;

import org.apache.camel.Exchange;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.camel.CamelMessage;
import akka.camel.javaapi.UntypedProducerActor;

public class HttpProducer extends UntypedProducerActor
{
    private ActorRef transformer;
    private String uri;

    public HttpProducer(ActorRef transformer, String uri)
    {
        this.transformer = transformer;
        this.uri = String.format("jetty://%s?bridgeEndpoint=true", uri);
    }
    
    public static Props props(final ActorRef transformer, final String uri)
    {
        return Props.create(HttpProducer.class, transformer, uri);
    }

    public String getEndpointUri()
    {
        // bridgeEndpoint=true makes the producer ignore the Exchange.HTTP_URI header, 
        // and use the endpoint's URI for request
        return uri;
    }

    // before producing messages to endpoints, producer actors can pre-process
    // them by overriding the onTransformOutgoingMessage method
    @Override
    public Object onTransformOutgoingMessage(Object message)
    {
        if (message instanceof CamelMessage)
        {
            CamelMessage camelMessage = (CamelMessage) message;
            Set<String> httpPath = new HashSet<String>();
            httpPath.add(Exchange.HTTP_PATH);
            return camelMessage.withHeaders(camelMessage.getHeaders(httpPath));
        }
        else
            return super.onTransformOutgoingMessage(message);
    }

    // instead of replying to the initial sender, producer actors can implement custom
    // response processing by overriding the onRouteResponse method
    @Override
    public void onRouteResponse(Object message)
    {
        transformer.forward(message, getContext());
    }
}
