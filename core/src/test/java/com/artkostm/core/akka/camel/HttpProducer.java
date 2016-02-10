package com.artkostm.core.akka.camel;

import java.util.HashSet;
import java.util.Set;

import org.apache.camel.Exchange;
import org.eclipse.jetty.server.Request;

import akka.actor.ActorRef;
import akka.camel.CamelMessage;
import akka.camel.javaapi.UntypedProducerActor;

public class HttpProducer extends UntypedProducerActor
{
    private ActorRef transformer;

    public HttpProducer(ActorRef transformer)
    {
        this.transformer = transformer;
    }
    
    private String proxedUrl = "jetty://http://www.google.by/webhp?hl=en?bridgeEndpoint=true";

    public String getEndpointUri()
    {
        // bridgeEndpoint=true makes the producer ignore the Exchange.HTTP_URI header, 
        // and use the endpoint's URI for request
        return proxedUrl;
    }

    // before producing messages to endpoints, producer actors can pre-process
    // them by overriding the onTransformOutgoingMessage method
    @Override
    public Object onTransformOutgoingMessage(Object message)
    {
        if (message instanceof CamelMessage)
        {
            CamelMessage camelMessage = (CamelMessage) message;
            final Request req = (Request) camelMessage.getHeaders().get("CamelHttpServletRequest");
            final String url = req.getHeader("proxy-url");
            proxedUrl = String.format("//jetty://http://%sbridgeEndpoint=true", url);
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
        System.out.println(message);
        transformer.forward(message, getContext());
    }
}
