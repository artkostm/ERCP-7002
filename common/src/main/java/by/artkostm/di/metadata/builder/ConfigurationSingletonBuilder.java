package by.artkostm.di.metadata.builder;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import by.artkostm.di.metadata.ConfigurationMetadata;
import by.artkostm.di.metadata.LifeCycleMetadata.Role;
import by.artkostm.di.metadata.impl.ConfigurationMetadataImpl;
import by.artkostm.di.util.MethodInvoker;

public class ConfigurationSingletonBuilder
{
    private static final Logger LOG = LogManager.getLogger(ConfigurationSingletonBuilder.class);
        
    public static ConfigurationMetadata buildConfigurationSingleton(Class<?> clazz, String name, Role role)
    {
        Object configObject = null;
        try
        {
            configObject = MethodInvoker.invokeConstroctor(clazz);
        }
        catch (Exception e)
        {
            LOG.warn("Cannot create instance of " + clazz.getName() + " class.", e);
        }
        final ConfigurationMetadata configMetadata = new ConfigurationMetadataImpl(configObject, name, role);
        return configMetadata;
    }
}
