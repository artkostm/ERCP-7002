package com.artkostm.core.service;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 
 * @author Artsiom_Chuiko
 *
 */
public class ServiceManager 
{
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    
    public void registerService(final Service service, final long delay, 
            final TimeUnit timeUnit) 
    {
        scheduler.schedule(service, delay, timeUnit);
        
    }

    public void registerService(final Service service, final long initialDelay, 
            final long period, final TimeUnit timeUnit) 
    {
        scheduler.scheduleAtFixedRate(service, initialDelay, period, timeUnit);
    }
    
    public static final String SESSION_SERVICE = "SESSION_SERVICE";
}
