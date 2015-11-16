package com.artkostm.configurator.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.artkostm.configurator.HiddenCall;

import groovy.lang.Closure;

public class ConfigLogger extends Closure<Object> implements HiddenCall
{

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    protected static final Logger LOG = LogManager.getLogger(ConfigLogger.class);

    public ConfigLogger(final Object owner)
    {
        super(owner);
    }
    
    @Override
    public Object doCall(final Object...args) 
    {
        if (args != null)
        {
            for (Object obj : args)
            {
                LOG.info(obj);
            }
        }
        return Closure.DONE;
    }
}
