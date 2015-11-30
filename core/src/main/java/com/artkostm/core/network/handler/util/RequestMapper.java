package com.artkostm.core.network.handler.util;

import io.netty.handler.codec.http.HttpMethod;

import java.lang.reflect.Method;
import java.util.List;

import com.artkostm.configurator.model.Metadata;
import com.artkostm.configurator.model.RouteConfig;
import com.artkostm.core.network.router.Router;

public class RequestMapper 
{
    public static void map(final Router<List<Method>> router, final Metadata configuration)
    {
        if (configuration == null)
        {
            return; //TODO: add logging here
        }
        final List<RouteConfig> configs = configuration.getRouteConfigList();
        for (RouteConfig config : configs)
        {
            router.addRoute(HttpMethod.valueOf(config.method().toUpperCase()), 
                   config.url(), config.controllers());
        }
    }
}
