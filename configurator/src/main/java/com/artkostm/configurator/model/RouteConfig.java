package com.artkostm.configurator.model;

import java.lang.reflect.Method;
import java.util.List;

public class RouteConfig
{
    private String method;
    private String url;
    private List<Method> controllers;
    
    public RouteConfig(final String method, final String url, final List<Method> controllers)
    {
        this.method = method;
        this.url = url;
        this.controllers = controllers;
    }

    public String method()
    {
        return method;
    }

    public void method(final String method)
    {
        this.method = method;
    }

    public String url()
    {
        return url;
    }

    public void url(final String url)
    {
        this.url = url;
    }

    public List<Method> controllers()
    {
        return controllers;
    }

    public void controllers(final List<Method> controllers)
    {
        this.controllers = controllers;
    }

    @Override
    public String toString()
    {
        return "RouteConfig [method=" + method + ", url=" + url + ", controllers="
            + controllers + "]";
    }
}
