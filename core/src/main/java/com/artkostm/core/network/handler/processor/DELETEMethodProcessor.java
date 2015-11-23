package com.artkostm.core.network.handler.processor;

public class DELETEMethodProcessor implements HttpMethodProcessor
{

    @Override
    public void process(Object request)
    {
        System.out.println("DELETE processor");
    }

}
