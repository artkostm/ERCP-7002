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
    
    public TemplateConfig getTemplate()
    {
        return template;
    }
    
    public void setTemplate(TemplateConfig template)
    {
        this.template = template;
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

    public void setGET(List<RouteConfig> gET)
    {
        GET = gET;
    }

    public List<RouteConfig> getPOST()
    {
        return POST;
    }

    public void setPOST(List<RouteConfig> pOST)
    {
        POST = pOST;
    }

    public List<RouteConfig> getDELETE()
    {
        return DELETE;
    }

    public void setDELETE(List<RouteConfig> dELETE)
    {
        DELETE = dELETE;
    }

    public List<RouteConfig> getPUT()
    {
        return PUT;
    }

    public void setPUT(List<RouteConfig> pUT)
    {
        PUT = pUT;
    }

    @Override
    public String toString() {
        return "AppConfig [template=" + template + ", netty=" + netty + ", GET=" + GET + ", POST=" + POST + ", DELETE="
                + DELETE + ", PUT=" + PUT + "]";
    }
}
