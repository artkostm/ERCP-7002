package com.artkostm.core.web.network.handler.method.processor;

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http.HttpObject;

import com.artkostm.core.web.controller.Context;

/**
 * 
 * @author Artsiom_Chuiko
 *
 */
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
