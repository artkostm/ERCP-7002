package com.artkostm.core.web;

import java.lang.reflect.Method;
import java.util.List;

import com.artkostm.core.web.network.router.Router;

/**
 * 
 * @author Artsiom_Chuiko
 *
 */
public interface Application extends Runnable, ApplicationConstants
{
    void configure();
    
    Router<List<Method>> router();
}
