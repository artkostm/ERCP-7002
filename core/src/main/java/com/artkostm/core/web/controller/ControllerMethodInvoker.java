package com.artkostm.core.web.controller;

import java.lang.reflect.Method;
import java.util.List;

/**
 * 
 * @author Artsiom_Chuiko
 *
 */
public class ControllerMethodInvoker 
{
    private static final Object[] EMPTY_ARGS = new Object[0];
    
    public static Result invoke(final Method method)
    {
        //TODO: check that method is static or isn't
        try 
        {
            method.setAccessible(true);
            final Object result = method.invoke(null, EMPTY_ARGS);
            return (Result) result;
        } 
        catch (Exception e) 
        {
            // TODO: add logging here
            e.printStackTrace();
            return null;
        }
    }
    
    public static Result invoke(final List<Method> methods)
    {
        for (Method method : methods)
        {
            return invoke(method);
        }
        return null;
    }
}
