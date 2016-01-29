package com.artkostm.core.configuration;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class ConfigUtils
{
    /**
     * 
     * @param fullMethodPath - example: com.full.package.path.ClassName.methodName,
     * where com.full.package.path.ClassName - full name of class 
     * that contains public static void methodName() method 
     * @return Method or null if cannot instantiate class instance
     */
    public static Method tryToGetMethodByName(final String fullMethodPath)
    {
        if (fullMethodPath == null || fullMethodPath.isEmpty())
        {
            return null;
        }
        
        final int ind = fullMethodPath.lastIndexOf('.');
        final String className = fullMethodPath.substring(0, ind);
        final String methodName = fullMethodPath.substring(ind+1);
        try 
        {
            final Class<?> cl = Class.forName(className);
            return cl.getMethod(methodName, new Class<?>[]{});
        } catch (Exception e) 
        {
            e.printStackTrace();//TODO: add logging here
        }
        return null;
    }
    
    /**
     * 
     * @param fullMethodPathesList - a list of full method paths
     * @see {com.artkostm.configurator.util.ConfigurationUtils.tryToGetMethodByName(String)}
     * @return a list of Methods, the list do not contain null values
     */
    public static List<Method> tryToGetListOfMethodsByNames(final List<String> fullMethodPathsList)
    {
        if (fullMethodPathsList == null || fullMethodPathsList.isEmpty())
        {
            return Collections.emptyList();
        }
        final List<Method> methods = new ArrayList<Method>(fullMethodPathsList.size());
        for (String fullMethodPath : fullMethodPathsList)
        {
            final Method method = tryToGetMethodByName(fullMethodPath);
            if (method != null)
            {
                methods.add(method);
            }
        }
        return methods;
    }
    
    private ConfigUtils()
    {}
}
