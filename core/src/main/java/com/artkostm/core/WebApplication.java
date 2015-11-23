package com.artkostm.core;

import java.net.InetSocketAddress;

import com.artkostm.configurator.model.Metadata;
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
            public Object getData()
            {
                return Thread.currentThread().getName()+"HelloData";
            }
        };
        final HttpServer server = new HttpServer(new InetSocketAddress("localhost", 8080));
        System.out.println(Thread.currentThread().getName()+":configure");
        configure(config);
        System.out.println(Thread.currentThread().getName()+":startApp");
        server.run();
    }
}
