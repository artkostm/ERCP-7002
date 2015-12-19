package com.artkostm.core.guice.module;

import java.net.InetSocketAddress;
import java.net.SocketAddress;

import com.artkostm.configurator.Configurator;
import com.artkostm.configurator.model.Metadata;
import com.artkostm.core.ApplicationConstants;
import com.artkostm.core.guice.annotation.Config.Host;
import com.artkostm.core.guice.annotation.Config.Port;
import com.artkostm.core.guice.annotation.Config.TemplateLoadingDir;
import com.google.inject.AbstractModule;

public abstract class ConfigurationModule extends AbstractModule implements ApplicationConstants
{   
    @Override
    protected void configure()
    {
        final Integer port = port() == 0 ? DEFAULT_PORT : port();
        bindConstant().annotatedWith(Port.class).to(port);
        
        final String host = host() == null ? LOCAL_HOST : host();
        bindConstant().annotatedWith(Host.class).to(host);
        
        final String templateLoadingDir = templateLoadingDir() == null ? 
            DEFAULT_DIRECTORY_FOR_TEMPLATE_LOADING : templateLoadingDir();
        bindConstant().annotatedWith(TemplateLoadingDir.class).to(templateLoadingDir);
        
        bind(SocketAddress.class).toInstance(new InetSocketAddress(host, port));
    }
    
    protected abstract Integer port();
    
    protected abstract String host();
    
    protected abstract String templateLoadingDir();
    
    protected abstract Metadata metadata();
    
    protected abstract Configurator configurator();
}
