package com.artkostm.core.web;

import com.artkostm.core.akka.configuration.RouteObject;
import com.artkostm.core.configuration.internal.AppConfig;
import com.artkostm.core.web.network.HttpServer;
import com.artkostm.core.web.network.router.Router;
import com.artkostm.template.TemplateCompiller;
import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * 
 * @author Artsiom_Chuiko
 *
 */
@Singleton
public abstract class WebApplication implements Application
{    
//    @Inject
//    private MethodRouterProvider routerProvider;
    @Inject
    private Router<RouteObject> router;
    
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
        
//        RequestMapper.map(routerProvider.get(), metadata);
        System.out.println();
        configure();
        
        server.run();
    }
    
    @Override
    public Router<RouteObject> router() 
    {
        return router;
    }
}
