package com.artkostm.core.network.handler.method.processor;

public class PUTMethodProcessor implements HttpMethodProcessor
{
    @Override
    public void process(Object request)
    {
        System.out.println("PUT processor");
    }
}
