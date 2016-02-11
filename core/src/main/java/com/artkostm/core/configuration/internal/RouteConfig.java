package com.artkostm.core.configuration.internal;

/**
 * 
 * @author Artsiom_Chuiko
 *
 */
public class RouteConfig
{
    private String url;
    private String controller;
    
    public RouteConfig()
    {}
    
    public RouteConfig(final String url, final String controller)
    {
        super();
        this.url = url;
        this.controller = controller;
    }

    public String getUrl()
    {
        return url;
    }
    
    public String getController()
    {
        return controller;
    }

    @Override
    public String toString()
    {
        return "Route [url=" + url + ", controller=" + controller + "]";
    }
}
