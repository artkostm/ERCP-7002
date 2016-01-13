package com.artkostm.core;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;

import com.artkostm.configurator.Configurator;
import com.artkostm.configurator.model.Metadata;
import com.artkostm.configurator.model.RouteConfig;
import com.artkostm.core.network.router.Router;

public interface Application extends Runnable, ApplicationConstants
{
    void configure();
    Router<List<Method>> router();
    Configurator configurator();
    
    public static final Metadata EMPTY_METADATA = new Metadata() 
    {
        @Override
        public List<RouteConfig> getRouteConfigList() 
        {
            return Collections.emptyList();
        }
    };
}
