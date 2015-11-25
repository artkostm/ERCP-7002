package com.artkostm.core.network.handler.method.processor;

public class POSTMethodProcessor implements HttpMethodProcessor
{
    @Override
    public void process(Object request)
    {
        System.out.println("POST processor");
    }
}
