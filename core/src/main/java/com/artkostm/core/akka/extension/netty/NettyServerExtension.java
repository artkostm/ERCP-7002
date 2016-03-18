package com.artkostm.core.akka.extension.netty;

import java.util.concurrent.atomic.AtomicBoolean;

import com.artkostm.core.configuration.BeanFactory;
import com.artkostm.core.configuration.internal.AppConfig;
import com.typesafe.config.Config;

import akka.actor.Extension;

public class NettyServerExtension implements Extension
{
    private final AppConfig appConfig;
    
    private final AtomicBoolean isRunning;
    
    public NettyServerExtension(final Config config)
    {
        this.appConfig = BeanFactory.create(config.getConfig("app"), AppConfig.class);
        isRunning = new AtomicBoolean(false);
    }
    
    public void run()
    {
        if (!isRunning.get())
        {
            isRunning.compareAndSet(false, true);
        }
    }
    
    public AppConfig config()
    {
        return appConfig;
    }
}
