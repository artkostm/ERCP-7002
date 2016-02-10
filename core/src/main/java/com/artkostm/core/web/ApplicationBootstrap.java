package com.artkostm.core.web;

import com.artkostm.core.configuration.ConfigurationProvider;
import com.artkostm.core.guice.module.HttpServerModule;
import com.google.inject.Guice;
import com.google.inject.Injector;

/**
 * 
 * @author Artsiom_Chuiko
 *
 */
public final class ApplicationBootstrap
{
    public static void run(final Class<? extends WebApplication> appClass, final String configPath)
    {
        final Injector injector = Guice.createInjector(
            new ConfigurationProvider(configPath), 
            new HttpServerModule());
        final WebApplication application = injector.getInstance(appClass);
        application.run();
    }
    
    public static void run(final Class<? extends WebApplication> appClass)
    {
        final Injector injector = Guice.createInjector(
            new ConfigurationProvider(),
            new HttpServerModule());
        final WebApplication application = injector.getInstance(appClass);
        application.run();
    }
    
    private ApplicationBootstrap()
    {}
}
