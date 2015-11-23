package com.artkostm.core.network.handler.processor;

public class GETMethodProcessor implements HttpMethodProcessor
{
    @Override
    public void process(Object request)
    {
        System.out.println("GET processor");
    }
}
