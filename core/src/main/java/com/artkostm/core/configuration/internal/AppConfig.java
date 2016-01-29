package com.artkostm.core.configuration.internal;

import java.util.List;

public class AppConfig
{
    private TemplateConfig template;
    private NettyConfig netty;
    private List<Rout> GET;
    
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
    
    public List<Rout> getGET()
    {
        return GET;
    }

    public void setGET(List<Rout> gET)
    {
        GET = gET;
    }

    @Override
    public String toString()
    {
        return "AppConfig [template=" + template + ", netty=" + netty + ", GET=" + GET + "]";
    }



    public static class Rout
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
            return "Rout [url=" + url + ", controller=" + controller + "]";
        }
    }
}
