package com.artkostm.core.controller;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ControllerRepository
{
    private final Map<Class<? extends Controller>, Controller> repository;
    
    public ControllerRepository()
    {
        repository = new HashMap<Class<? extends Controller>, Controller>();
    }
    
    @SuppressWarnings("unchecked")
    public boolean registerController(final Method method)
    {
        if (method == null)
        {
            return false;
        }
        
        final Class<?> controllerClass = method.getDeclaringClass();
        final Controller controller = repository.get(controllerClass);
        if (controller == null)
        {
            try
            {
                final Controller newController = (Controller) controllerClass.getConstructor().newInstance();
                repository.put((Class<? extends Controller>)controllerClass, newController);
                return true;
            }
            catch (Exception e)
            {
                e.printStackTrace();//TODO: add logging here
                return false;
            }
        }
        return true;
    }
    
    public Controller get(final Method method)
    {
        final Class<?> controllerClass = method.getDeclaringClass();
        return repository.get(controllerClass);
    }
    
    public Controller get(final Class<? extends Controller> clazz)
    {
        return repository.get(clazz);
    }
    
    public static void registerControllers(final ControllerRepository repo, final List<Method> controllers)
    {
        for (Method method : controllers)
        {
            repo.registerController(method);
        }
    }
}
