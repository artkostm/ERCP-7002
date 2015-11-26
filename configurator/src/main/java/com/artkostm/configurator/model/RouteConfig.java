package com.artkostm.configurator.model;

import java.lang.reflect.Method;

public class RouteConfig
{
    private String method;
    private String url;
    private Method controller;
    
    public RouteConfig(final String method, final String url, final Method controller)
    {
        this.method = method;
        this.url = url;
        this.controller = controller;
    }

    public String method()
    {
        return method;
    }

    public void method(String method)
    {
        this.method = method;
    }

    public String url()
    {
        return url;
    }

    public void url(String url)
    {
        this.url = url;
    }

    public Method controller()
    {
        return controller;
    }

    public void controller(Method controller)
    {
        this.controller = controller;
    }

    @Override
    public String toString()
    {
        return "RouteConfig [method=" + method + ", url=" + url + ", controller="
            + controller + "]";
    }
}
