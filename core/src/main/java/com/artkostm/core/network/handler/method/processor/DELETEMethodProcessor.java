package com.artkostm.core.network.handler.method.processor;

public class DELETEMethodProcessor implements HttpMethodProcessor
{

    @Override
    public void process(Object request)
    {
        System.out.println("DELETE processor");
    }

}
