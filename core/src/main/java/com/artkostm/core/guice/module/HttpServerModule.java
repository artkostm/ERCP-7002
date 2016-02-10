package com.artkostm.core.guice.module;

import com.artkostm.core.guice.annotation.Config.Ssl;
import com.google.inject.AbstractModule;

/**
 * 
 * @author Artsiom_Chuiko
 *
 */
public class HttpServerModule extends AbstractModule
{
    @Override
    protected void configure()
    {
        bindConstant().annotatedWith(Ssl.class).to(System.getProperty("ssl") != null);
    }
}
