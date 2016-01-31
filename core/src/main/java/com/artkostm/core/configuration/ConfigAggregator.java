package com.artkostm.core.configuration;

import com.artkostm.core.ApplicationConstants;
import com.artkostm.core.configuration.internal.AppConfig;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigResolveOptions;

public final class ConfigAggregator implements ApplicationConstants
{
    private Config main;
    private static final ConfigAggregator aggregator = new ConfigAggregator();
    
    public static synchronized ConfigAggregator load(final String path)
    {
        aggregator.main = ConfigFactory.parseResources(ConfigAggregator.class, path)
                .resolve(ConfigResolveOptions.defaults().setUseSystemEnvironment(true));;
        return aggregator;
    }
    
    public static synchronized ConfigAggregator load()
    {
        aggregator.main = ConfigFactory.defaultApplication();
        return aggregator;
    }
    
    public static synchronized ConfigAggregator aggregate(final String path)
    {
        final Config cnfg = ConfigFactory.load(path);
        aggregator.main = aggregator.main.withFallback(cnfg);
        return aggregator;
    }
    
    public static synchronized ConfigAggregator aggregate(final Config conf)
    {
        aggregator.main = aggregator.main.withFallback(conf);
        return aggregator;
    }
    
    public int port()
    {
        return main.hasPath("app.netty.port") ? 
            main.getInt("app.netty.port") : DEFAULT_PORT;
    }
    
    public String template()
    {
        return main.hasPath("app.template.directory") ? 
            main.getString("app.template.directory") : DEFAULT_DIRECTORY_FOR_TEMPLATE_LOADING;
    }
    
    public String host()
    {
        return main.hasPath("app.netty.host") ? 
            main.getString("app.netty.host") : LOCAL_HOST;
    }
    
    public Config configuration()
    {
        return main;
    }
    
    public AppConfig app()
    {
        if (main.hasPath("app"))
        {
            return BeanFactory.create(main.getConfig("app"), AppConfig.class);
        }
        return new AppConfig();
    }
    
    private ConfigAggregator()
    {}
}
