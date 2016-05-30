package com.artkostm.core.akka.http.message;

import com.artkostm.core.akka.configuration.RouteObject;
import com.artkostm.core.akka.http.HttpMethods;
import com.artkostm.core.web.network.router.RouteResult;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpRequest;

public class AppExternalMessage implements HttpMessage
{
    private static final long serialVersionUID = -9217663149692740007L;
    
    private Object payload;
    
    public AppExternalMessage(Object payload)
    {
        this.payload = payload;
    }
    
    @Override
    public ChannelHandlerContext context()
    {
        return null;
    }

    @Override
    public void context(ChannelHandlerContext context)
    {}

    @Override
    public HttpMethods method()
    {
        return null;
    }

    @Override
    public RouteResult<RouteObject> routeResult()
    {
        return null;
    }

    @Override
    public HttpRequest request()
    {
        return null;
    }

    @Override
    public Object payload()
    {
        return payload;
    }

    @Override
    public void payload(Object obj)
    {
        this.payload = obj;
    }
}
