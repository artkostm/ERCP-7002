package com.artkostm.core.configuration;

import com.artkostm.core.ApplicationConstants;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

public final class ConfigAggregator implements ApplicationConstants
{
    private Config main;
    private static final ConfigAggregator aggregator = new ConfigAggregator();
    
    public static ConfigAggregator load(final String path)
    {
        aggregator.main = ConfigFactory.load(path);
        return aggregator;
    }
    
    public static ConfigAggregator load()
    {
        aggregator.main = ConfigFactory.defaultApplication();
        return aggregator;
    }
    
    public static ConfigAggregator aggregate(final String path)
    {
        final Config cnfg = ConfigFactory.load(path);
        aggregator.main = aggregator.main.withFallback(cnfg);
        return aggregator;
    }
    
    public static ConfigAggregator aggregate(final Config conf)
    {
        aggregator.main = aggregator.main.withFallback(conf);
        return aggregator;
    }
    
    public static int port()
    {
        return aggregator.main.hasPath("app.netty.port") ? 
            aggregator.main.getInt("app.netty.port") : DEFAULT_PORT;
    }
    
    public static String template()
    {
        return aggregator.main.hasPath("app.template.directory") ? 
            aggregator.main.getString("app.template.directory") : DEFAULT_DIRECTORY_FOR_TEMPLATE_LOADING;
    }
    
    public static Config configuration()
    {
        return aggregator.main;
    }
    
    public static void routees()
    {
        if (aggregator.main.hasPath("app.POST"))
            System.out.println(aggregator.main.getAnyRefList("app.POST"));
    }
    
    private ConfigAggregator()
    {}
}
