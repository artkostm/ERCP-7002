package com.artkostm.configurator.model.closure;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.artkostm.configurator.HiddenCall;
import com.artkostm.configurator.model.RouteConfig;

import groovy.lang.Closure;

public class RoutingRepository extends Closure<Object> implements HiddenCall
{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String POST = "post";
    public static final String GET = "get";
    public static final String PUT = "put";
    public static final String DELETE = "delete";
    
    private final List<RouteConfig> repository;
    private final String name;
    
    public RoutingRepository(Object owner, String name)
    {
        super(owner);
        repository = new ArrayList<RouteConfig>();
        this.name = name;
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public Object doCall(final Object... args)
    {
        if (args == null || args.length != 2)
        {
            throw new RuntimeException(":(");
        }
        
        if (args[1] instanceof List<?>)
        {
            final List<Method> controllers = (List<Method>) args[1];
            for (Method controller : controllers)
            {
                repository.add(new RouteConfig(name(), (String)args[0], controller));
            }
        }
        else
        {
            final String methodPath = (String) args[1];
            int ind = methodPath.lastIndexOf('.');
            final String className = methodPath.substring(0, ind);
            final String methodName = methodPath.substring(ind+1);
            System.out.println("class: " + className + "| method: " + methodName);
            repository.add(new RouteConfig(name(), (String)args[0], (Method)args[1]));
        }

        return Closure.DONE;
    }
    
    @Override
    public String name()
    {
        return name;
    }
    
    public List<RouteConfig> getRepository()
    {
        return Collections.unmodifiableList(repository);//TODO: check this: unmodifiable or modifiable
    }
    
    private Me
}
