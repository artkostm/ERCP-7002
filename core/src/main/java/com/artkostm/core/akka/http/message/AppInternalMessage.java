package com.artkostm.core.akka.http.message;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpRequest;

import com.artkostm.core.akka.configuration.RouteObject;
import com.artkostm.core.akka.http.HttpMethods;
import com.artkostm.core.web.network.router.RouteResult;

public class AppInternalMessage implements HttpMessage
{
    private static final long serialVersionUID = -2461683137831033776L;
    
    private ChannelHandlerContext context;
    private final HttpRequest request;
    private RouteResult<RouteObject> routeResult;
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

    @Override
    public String toString()
    {
        return "AppInternalMessage [context=" + context + ", request=" + request + ", payload=" + payload + "]";
    }

    @Override
    public RouteResult<RouteObject> routeResult()
    {
        return routeResult;
    }
    
    public void setRouteResult(RouteResult<RouteObject> routeResult)
    {
        this.routeResult = routeResult;
    }
}
