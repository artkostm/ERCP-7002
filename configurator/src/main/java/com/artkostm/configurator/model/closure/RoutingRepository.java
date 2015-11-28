package com.artkostm.configurator.model.closure;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.artkostm.configurator.HiddenCall;
import com.artkostm.configurator.model.RouteConfig;
import com.artkostm.configurator.util.ConfigurationUtils;

import groovy.lang.Closure;

public class RoutingRepository extends Closure<Object> implements HiddenCall
{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    //TODO: add more http methods
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
            throw new RuntimeException(":(");//TODO: add description
        }
        
        if (args[1] instanceof List<?>)
        {
            final List<Method> controllers 
                    = ConfigurationUtils.tryToGetListOfMethodsByNames((List<String>) args[1]);
            repository.add(new RouteConfig(name(), (String)args[0], controllers));
        }
        else
        {
            final Method controller = ConfigurationUtils.tryToGetMethodByName((String) args[1]);
            repository.add(new RouteConfig(name(), (String)args[0], Arrays.asList(controller)));
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
}
