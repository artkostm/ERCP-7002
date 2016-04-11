package com.artkostm.core.akka.actors;

public abstract class ConfCamelControllerActor extends CamelControllerActor
{
    private final String consumerUri;
    private final String producerUri;
    
    public ConfCamelControllerActor(final String uri1, final String uri2)
    {
        this.consumerUri = uri1;
        this.producerUri = uri2;
    }

    @Override
    protected String getConsumerUri()
    {
        return consumerUri;
    }
    
    @Override
    protected String getProducerUri()
    {
        return producerUri;
    }
}
