package com.artkostm.core;

import java.lang.reflect.Method;
import java.util.List;

import com.artkostm.core.configuration.internal.AppConfig;
import com.artkostm.core.network.HttpServer;
import com.artkostm.core.network.handler.util.RequestMapper;
import com.artkostm.core.network.router.MethodRouterProvider;
import com.artkostm.core.network.router.Router;
import com.artkostm.template.TemplateCompiller;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public abstract class WebApplication implements Application
{    
    @Inject
    private MethodRouterProvider routerProvider;
    
    @Inject
    private AppConfig metadata;
    
    @Inject
    private HttpServer server;
    
    @Override
    public void run()
    {
        if (metadata != null && metadata.getTemplate() != null)
        {
            TemplateCompiller.configure(metadata.getTemplate().getDirectory());
        }
        
        RequestMapper.map(routerProvider.get(), metadata);
        server.run();
    }
    
    @Override
    public Router<List<Method>> router() 
    {
        return routerProvider.get();
    }
}
