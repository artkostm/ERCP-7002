package com.artkostm.core.akka.http.message;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpRequest;

import com.artkostm.core.akka.http.HttpMethods;

public class AppInternalMessage implements HttpMessage
{
    private ChannelHandlerContext context;
    private final HttpRequest request;
    private Object payload;
    
    public AppInternalMessage(final HttpRequest request, final ChannelHandlerContext context)
    {
        this.context = context;
        this.request = request;
    }
    
    public AppInternalMessage(final HttpRequest request, final ChannelHandlerContext context, final Object payload)
    {
        this.context = context;
        this.request = request;
        this.payload = payload;
    }
    
    @Override
    public ChannelHandlerContext context()
    {
        return context;
    }

    @Override
    public void context(ChannelHandlerContext context)
    {
        this.context = context;
    }

    @Override
    public HttpMethods method()
    {
        return null;
    }

    @Override
    public HttpRequest request()
    {
        return request;
    }

    @Override
    public Object payload()
    {
        return payload;
    }

    @Override
    public void payload(Object obj)
    {
        payload = obj;
    }
}
