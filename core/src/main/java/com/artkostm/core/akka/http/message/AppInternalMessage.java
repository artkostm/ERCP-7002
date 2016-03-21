package com.artkostm.core.akka.http.message;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpRequest;

import com.artkostm.core.akka.http.HttpMethods;

public class AppInternalMessage implements HttpMessage
{
    private ChannelHandlerContext context;
    private final HttpRequest request;
    
    public AppInternalMessage(final HttpRequest request, final ChannelHandlerContext context)
    {
        this.context = context;
        this.request = request;
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
}
