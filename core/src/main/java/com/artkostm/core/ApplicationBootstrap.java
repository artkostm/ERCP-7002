package com.artkostm.core;

import com.artkostm.core.configuration.Configuration;
import com.artkostm.core.guice.module.HttpServerModule;
import com.google.inject.Guice;
import com.google.inject.Injector;

public class ApplicationBootstrap
{
    public static void run(final Class<? extends WebApplication> appClass, final String configPath)
    {
        final Injector injector = Guice.createInjector(
            new Configuration(configPath), 
            new HttpServerModule());
        final WebApplication application = injector.getInstance(appClass);
        application.configure();
        application.run();
    }
    
    /**
     * 
     * @param appClass
     */
    public static void run(final Class<? extends WebApplication> appClass)
    {
        final Injector injector = Guice.createInjector(
            new Configuration(),
            new HttpServerModule());
        final WebApplication application = injector.getInstance(appClass);
        application.configure();
        application.run();
    }
}
