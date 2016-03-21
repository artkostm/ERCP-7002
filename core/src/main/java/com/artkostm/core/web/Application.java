package com.artkostm.core.web;

import com.artkostm.core.akka.configuration.RouteObject;
import com.artkostm.core.web.network.router.Router;

/**
 * 
 * @author Artsiom_Chuiko
 *
 */
public interface Application extends Runnable, ApplicationConstants
{
    void configure();
    
    Router<RouteObject> router();
}
