package com.artkostm.configurator.model;

import com.artkostm.configurator.util.ConfigLogger;

public class Configuration implements Metadata
{
    private final ConfigLogger log;
    
    public Configuration()
    {
        log = new ConfigLogger(this);
    }
    
    @Override
    public Object getData()
    {
        return null;
    }

    public ConfigLogger getLog()
    {
        return log;
    }
}