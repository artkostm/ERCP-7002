package com.artkostm.core.network.handler.util;

import io.netty.handler.codec.http.HttpMethod;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;

import com.artkostm.configurator.model.Metadata;
import com.artkostm.configurator.model.RouteConfig;
import com.artkostm.core.configuration.ConfigUtils;
import com.artkostm.core.configuration.internal.AppConfig;
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
    
    public static void map(final Router<List<Method>> router, final AppConfig appConfig)
    {   
        if (appConfig.getGET() != null)
        for (com.artkostm.core.configuration.internal.RouteConfig rc : appConfig.getGET())
        {
            router.addRoute(HttpMethod.GET, rc.getUrl(), Collections.singletonList(ConfigUtils.tryToGetMethodByName(rc.getController())));
        }
        
        if (appConfig.getPOST() != null)
        for (com.artkostm.core.configuration.internal.RouteConfig rc : appConfig.getPOST())
        {
            router.addRoute(HttpMethod.POST, rc.getUrl(), Collections.singletonList(ConfigUtils.tryToGetMethodByName(rc.getController())));
        }
        
        if (appConfig.getPUT() != null)
        for (com.artkostm.core.configuration.internal.RouteConfig rc : appConfig.getPUT())
        {
            router.addRoute(HttpMethod.PUT, rc.getUrl(), Collections.singletonList(ConfigUtils.tryToGetMethodByName(rc.getController())));
        }
        
        if (appConfig.getDELETE() != null)
        for (com.artkostm.core.configuration.internal.RouteConfig rc : appConfig.getDELETE())
        {
            router.addRoute(HttpMethod.DELETE, rc.getUrl(), Collections.singletonList(ConfigUtils.tryToGetMethodByName(rc.getController())));
        }
    }
}
