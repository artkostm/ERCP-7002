package com.artkostm.core.configuration.internal;

import java.util.List;

/**
 * 
 * @author Artsiom_Chuiko
 *
 */
public class AppConfig
{
    private TemplateConfig template;
    private NettyConfig netty;
    private List<RouteConfig> GET;
    private List<RouteConfig> POST;
    private List<RouteConfig> DELETE;
    private List<RouteConfig> PUT;
    
    public AppConfig()
    {}
    
    public AppConfig(TemplateConfig template, NettyConfig netty, List<RouteConfig> gET, 
        List<RouteConfig> pOST, List<RouteConfig> dELETE, List<RouteConfig> pUT)
    {
        super();
        this.template = template;
        this.netty = netty;
        GET = gET;
        POST = pOST;
        DELETE = dELETE;
        PUT = pUT;
    }

    public TemplateConfig getTemplate()
    {
        return template;
    }
    
    public NettyConfig getNetty()
    {
        return netty;
    }
    
    public void setNetty(NettyConfig netty)
    {
        this.netty = netty;
    }
    
    public List<RouteConfig> getGET()
    {
        return GET;
    }

    public List<RouteConfig> getPOST()
    {
        return POST;
    }

    public List<RouteConfig> getDELETE()
    {
        return DELETE;
    }

    public List<RouteConfig> getPUT()
    {
        return PUT;
    }

    @Override
    public String toString() {
        return "AppConfig [template=" + template + ", netty=" + netty + ", GET=" + GET + ", POST=" + POST + ", DELETE="
                + DELETE + ", PUT=" + PUT + "]";
    }
}
