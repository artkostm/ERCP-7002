package com.artkostm.configurator.model.closure;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.artkostm.configurator.HiddenCall;
import com.artkostm.configurator.Main.Route;

import groovy.lang.Closure;

public class PostRoutingRepository extends Closure<Object> implements HiddenCall
{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    private final List<Route> repository;

    public PostRoutingRepository(Object owner)
    {
        super(owner);
        repository = new ArrayList<Route>();
    }
    
    @Override
    public Object doCall(Object... args)
    {
        if (args == null || args.length != 2)
        {
            throw new RuntimeException(":(");
        }
        
        repository.add(new Route(name(), (String)args[0], (String)args[1]));
        return Closure.DONE;
    }
    
    @Override
    public String name()
    {
        return "post";
    }
    
    public List<Route> getRepository()
    {
        return Collections.unmodifiableList(repository);//TODO: check this: unmodifiable or modifiable
    }
}
