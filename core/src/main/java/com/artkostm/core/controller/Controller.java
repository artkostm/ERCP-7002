package com.artkostm.core.controller;

import com.artkostm.core.controller.session.Session;
import com.artkostm.template.TemplateCompiller;

public abstract class Controller extends Results
{
    protected static String view(final String viewName, final Object data)
    {
        //TODO:add template compilation 
        return null;
    }
    
    public static String view(final String viewName)
    {
        final String page = TemplateCompiller.compileTemplate(viewName, "context", context());
        return page;
    }
    
    public static Context context()
    {
        return Context.current();
    }
    
    public static Session session()
    {
        return context().getSession();
    }
}
