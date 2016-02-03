package com.artkostm.core.guice;

import java.lang.reflect.Method;
import java.util.List;

import com.artkostm.configurator.model.Metadata;
import com.artkostm.core.guice.annotation.Config.Host;
import com.artkostm.core.guice.annotation.Config.Port;
import com.artkostm.core.guice.annotation.Config.TemplateLoadingDir;
import com.artkostm.core.web.network.router.MethodRouterProvider;
import com.artkostm.core.web.network.router.Router;
import com.google.inject.Inject;

public class TestCl
{
    private final String content;
    
    @Inject
    @TemplateLoadingDir 
    private String dir;
    
    @Inject
    private MethodRouterProvider routerProvider;
    
    @Inject
    private Metadata metadata;
    
    @Inject
    public TestCl(@Port final Integer port, 
        @Host final String host)
    {
        content = host + ":" + String.valueOf(port);
    }
    
    public Router<List<Method>> router()
    {
        return routerProvider.get();
    }
    
    public Metadata Metadata()
    {
        return metadata;
    }

    @Override
    public String toString()
    {
        return "TestCl [content=" + content + ", dir="+ dir +"] \n " + routerProvider.get();
    }
}
