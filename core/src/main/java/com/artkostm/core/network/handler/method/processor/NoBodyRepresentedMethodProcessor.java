package com.artkostm.core.network.handler.method.processor;

import io.netty.handler.codec.http.HttpObject;

import com.artkostm.core.controller.Context;

public class NoBodyRepresentedMethodProcessor implements HttpMethodProcessor
{
    @Override
    public void process(HttpObject request, Context context)
    {
        
    }
}
