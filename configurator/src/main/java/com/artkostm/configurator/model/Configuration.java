package com.artkostm.configurator.model;

import java.util.List;

public class Configuration implements Metadata
{
    private final List<RouteConfig> routeConfigList;
    
    public Configuration(final List<RouteConfig> routeConfigList) 
    {
        this.routeConfigList = routeConfigList;
    }
    
    @Override
    public List<RouteConfig> getRouteConfigList() 
    {
        return routeConfigList;
    }
    
}