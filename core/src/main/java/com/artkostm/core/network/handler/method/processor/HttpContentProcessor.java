package com.artkostm.core.network.handler.method.processor;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.LastHttpContent;

import com.artkostm.core.controller.Context;
import com.artkostm.core.network.handler.content.BodyConsumer;

public class HttpContentProcessor implements HttpMethodProcessor, BodyConsumer
{
    private ByteBuf byteBuf;
    
    @Override
    public void chunk(final HttpContent content)
    {
        byteBuf.writeBytes(content.content());
    }

    @Override
    public Object finished()
    {
        //TODO: read bytes from byteBuf and clear it
        byteBuf.clear();
        byteBuf = null;
        return null;
    }

    @Override
    public void handleError(final Throwable t)
    {
        //TODO: add logging here
    }

    @Override
    public void process(final HttpObject request, final Context context)
    {
        try
        {
            if (byteBuf == null)
            {
                byteBuf = Unpooled.buffer();
            }
            chunk((HttpContent) request);
            if (request instanceof LastHttpContent)
            {
                finished();
            }
        }
        catch (Exception e)
        {
            handleError(e);
        }
    }
}
