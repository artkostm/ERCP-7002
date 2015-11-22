package com.artkostm.configurator.model.closure;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.artkostm.configurator.HiddenCall;
import com.artkostm.configurator.Main.Route;

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
    
    private final List<Route> repository;
    private final String name;
    
    public RoutingRepository(Object owner, String name)
    {
        super(owner);
        repository = new ArrayList<Route>();
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
            final List<String> controllers = (List<String>) args[1];
            for (String controller : controllers)
            {
                repository.add(new Route(name(), (String)args[0], controller));
            }
        }
        else
        {
            repository.add(new Route(name(), (String)args[0], (String)args[1]));
        }

        return Closure.DONE;
    }
    
    @Override
    public String name()
    {
        return name;
    }
    
    public List<Route> getRepository()
    {
        return Collections.unmodifiableList(repository);//TODO: check this: unmodifiable or modifiable
    }
}
