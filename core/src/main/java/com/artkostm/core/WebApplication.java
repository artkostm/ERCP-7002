package com.artkostm.core;

import java.lang.reflect.Method;
import java.util.List;

import com.artkostm.configurator.Configurator;
import com.artkostm.configurator.model.Metadata;
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
    private Metadata metadata;
    
    @Inject
    private Configurator configurator;
    
    @Inject
    private HttpServer server;
    
    private boolean running;
    
    @Override
    public void run()
    {
        if (!running)
        {
            TemplateCompiller.configure(configurator.getDirectoryForTemplateLoading());
            RequestMapper.map(routerProvider.get(), metadata);
            server.run();
            running = true;
        }
    }
    
    @Override
    public Configurator configurator() 
    {
        return configurator;
    }
    
    @Override
    public Router<List<Method>> router() 
    {
        return routerProvider.get();
    }
}
