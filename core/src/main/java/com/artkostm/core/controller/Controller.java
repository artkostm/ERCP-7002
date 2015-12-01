package com.artkostm.core.controller;

public abstract class Controller extends Results
{
    protected static String view(final String viewName, final Object data)
    {
        //TODO:add template compilation 
        return null;
    }
    
    protected static String view(final String viewName)
    {
        //TODO:add template compilation 
        return null;
    }
    
    public static Context context()
    {
        return Context.current();
    }
}
