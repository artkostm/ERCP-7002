package com.artkostm.core.configuration;

import com.artkostm.configurator.Configurator;
import com.artkostm.configurator.model.Metadata;
import com.artkostm.core.ApplicationConstants;
import com.artkostm.core.guice.module.ConfigurationModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;

public class Configuration extends ConfigurationModule
{
    private final Configurator configurator;
    private final Metadata metadata;
    
    public Configuration(final String configFilePath)
    {
        configurator = new Configurator();
        metadata = configurator.createConfiguration(configFilePath);
    }
    
    public Configuration()
    {
        configurator = new Configurator();
        metadata = configurator.createConfiguration(null);
    }
    
    @Override
    protected Integer port()
    {
        return configurator.getPort();
    }

    @Override
    protected String templateLoadingDir()
    {
        return configurator.getDirectoryForTemplateLoading();
    }

    @Override
    protected String host()
    {
        return ApplicationConstants.LOCAL_HOST;
    }

    @Override 
    @Provides 
    @Singleton
    protected Metadata metadata()
    {
        return metadata;
    }

    @Override 
    @Provides 
    @Singleton
    protected Configurator configurator()
    {
        return configurator;
    }
}
