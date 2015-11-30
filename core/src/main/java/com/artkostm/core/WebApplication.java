package com.artkostm.core;

import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.util.List;

import com.artkostm.configurator.Configurator;
import com.artkostm.configurator.model.Metadata;
import com.artkostm.core.network.HttpServer;
import com.artkostm.core.network.handler.util.RequestMapper;
import com.artkostm.core.network.router.Router;

public abstract class WebApplication implements Application
{
    private final Configurator configurator;
    private final Router<List<Method>> router;
    
    public WebApplication() 
    {
        configurator = new Configurator();
        router = new Router<List<Method>>();
    }
    
    @Override
    public void run()
    {
        final Metadata configuration = configure();
        RequestMapper.map(router, configuration);
        final int port = configurator.getPort() == 0 ? DEFAULT_PORT : configurator.getPort();
        final HttpServer server = new HttpServer(new InetSocketAddress(LOCAL_HOST, port));
        server.run();
    }
    
    @Override
    public Configurator configurator() 
    {
        return configurator;
    }
    
    @Override
    public Router<List<Method>> router() 
    {
        return router;
    }
}
