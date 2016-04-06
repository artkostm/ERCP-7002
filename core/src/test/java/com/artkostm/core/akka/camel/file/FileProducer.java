package com.artkostm.core.akka.camel.file;

import akka.camel.CamelMessage;
import akka.camel.javaapi.UntypedProducerActor;

public class FileProducer extends UntypedProducerActor
{
    @Override
    public String getEndpointUri()
    {
        return "file:D:\\?fileName=transactions.csv.out";
    }
    
    @Override
    public Object onTransformOutgoingMessage(Object message)
    {
        if (message instanceof CamelMessage)
        {
            final CamelMessage camelMessage = (CamelMessage) message;
            final String body = camelMessage.getBodyAs(String.class, camelContext());
            return new CamelMessage(body.replace("\r\n", ", "), camelMessage.getHeaders());
        }
        else
            return super.onTransformOutgoingMessage(message);
    }
    
    @Override
    public void onRouteResponse(Object message)
    {
        getSender().tell(message, getSelf());
    }
}
