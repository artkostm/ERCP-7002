package com.artkostm.core.web;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;

import com.artkostm.configurator.model.Metadata;
import com.artkostm.configurator.model.RouteConfig;
import com.artkostm.core.web.network.router.Router;

public interface Application extends Runnable, ApplicationConstants
{
    void configure();
    Router<List<Method>> router();
    
    public static final Metadata EMPTY_METADATA = new Metadata() 
    {
        @Override
        public List<RouteConfig> getRouteConfigList() 
        {
            return Collections.emptyList();
        }
    };
}
