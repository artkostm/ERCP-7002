package com.artkostm.core.network.handler.processor;

public class POSTMethodProcessor implements HttpMethodProcessor
{
    @Override
    public void process(Object request)
    {
        System.out.println("POST processor");
    }
}
