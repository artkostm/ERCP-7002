package com.artkostm.core.akka.extension.netty;

import com.artkostm.core.configuration.BeanFactory;
import com.artkostm.core.configuration.internal.AppConfig;
import com.typesafe.config.Config;

import akka.actor.Extension;

public class NettyServerExtension implements Extension
{
    private final AppConfig appConfig;
    
    public NettyServerExtension(final Config config)
    {
        this.appConfig = BeanFactory.create(config.getConfig("app"), AppConfig.class);;
    }
    
    public AppConfig config()
    {
        return appConfig;
    }
}
