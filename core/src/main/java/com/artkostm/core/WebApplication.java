package com.artkostm.core;

import java.net.InetSocketAddress;
import java.util.List;

import com.artkostm.configurator.model.Metadata;
import com.artkostm.configurator.model.RouteConfig;
import com.artkostm.core.network.HttpServer;

public abstract class WebApplication implements Application
{
    @Override
    public void run()
    {
        System.out.println(Thread.currentThread().getName()+":getConfiguration");
        final Metadata config = new Metadata()
        {
            @Override
            public List<RouteConfig> getRouteConfigList() 
            {
                return null;
            }
        };
        final HttpServer server = new HttpServer(new InetSocketAddress("localhost", 8080));
        System.out.println(Thread.currentThread().getName()+":configure");
        configure(config);
        System.out.println(Thread.currentThread().getName()+":startApp");
        server.run();
    }
}
