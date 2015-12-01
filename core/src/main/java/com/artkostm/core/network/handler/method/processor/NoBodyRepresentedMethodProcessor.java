package com.artkostm.core.network.handler.method.processor;

import com.artkostm.core.controller.Context;

public class NoBodyRepresentedMethodProcessor implements HttpMethodProcessor
{
    @Override
    public Context process(Object request) 
    {
        return null;
    }
}
