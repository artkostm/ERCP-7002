package com.artkostm.core.configuration;

import com.artkostm.core.configuration.internal.AppConfig;
import com.artkostm.core.guice.module.ConfigurationModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.typesafe.config.Config;

/**
 * 
 * @author Artsiom_Chuiko
 *
 */
public class ConfigurationProvider extends ConfigurationModule
{
    public ConfigurationProvider(final String configFilePath)
    {
        ConfigAggregator.load(configFilePath);
    }
    
    public ConfigurationProvider()
    {
        ConfigAggregator.load();
    }
    
    @Override
    protected Integer port()
    {
        return ConfigAggregator.aggregator().port();
    }

    @Override
    protected String templateLoadingDir()
    {
        return ConfigAggregator.aggregator().template();
    }

    @Override
    protected String host()
    {
        return ConfigAggregator.aggregator().host();
    }

    @Override 
    @Provides 
    @Singleton
    protected AppConfig applicationConfiguration()
    {
        return ConfigAggregator.aggregator().app();
    }
    
    protected Config config()
    {
        return ConfigAggregator.aggregator().configuration();
    }
}
