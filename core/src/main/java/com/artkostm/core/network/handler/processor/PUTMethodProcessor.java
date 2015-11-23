package com.artkostm.core.network.handler.processor;

public class PUTMethodProcessor implements HttpMethodProcessor
{
    @Override
    public void process(Object request)
    {
        System.out.println("PUT processor");
    }
}
