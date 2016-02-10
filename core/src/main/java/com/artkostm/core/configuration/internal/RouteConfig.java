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
    
    public String getUrl()
    {
        return url;
    }
    
    public void setUrl(String url)
    {
        this.url = url;
    }
    
    public String getController()
    {
        return controller;
    }
    
    public void setController(String controller)
    {
        this.controller = controller;
    }

    @Override
    public String toString()
    {
        return "Route [url=" + url + ", controller=" + controller + "]";
    }
}
