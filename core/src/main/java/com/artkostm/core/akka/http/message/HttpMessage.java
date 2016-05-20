package com.artkostm.core.akka.http.message;

import io.netty.handler.codec.http.HttpRequest;

import com.artkostm.core.akka.configuration.RouteObject;
import com.artkostm.core.akka.http.HttpMethods;
import com.artkostm.core.netty.ChannelHandlerContexAware;
import com.artkostm.core.web.network.router.RouteResult;

public interface HttpMessage extends ChannelHandlerContexAware
{
    HttpMethods method();
    RouteResult<RouteObject> routeResult();
    HttpRequest request();
    Object payload();
    void payload(Object obj);
}
