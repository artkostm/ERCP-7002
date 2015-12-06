package com.artkostm.core.network.handler.method.processor;

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http.HttpObject;

import com.artkostm.core.controller.Context;

public class HttpContentProcessor implements HttpMethodProcessor<ByteBuf>
{
    @SuppressWarnings("unused")
    private ByteBuf byteBuf;

    @Override
    public ByteBuf process(HttpObject request, Context context) 
    {
        return null;
    }

    
}
