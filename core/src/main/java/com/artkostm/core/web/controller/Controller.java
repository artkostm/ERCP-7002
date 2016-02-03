package com.artkostm.core.web.controller;

import java.util.Map;

import com.artkostm.core.web.controller.session.Session;
import com.artkostm.template.TemplateCompiller;

public abstract class Controller extends Results
{
    protected static String view(final String viewName, final Map<String, Object> data)
    {
        data.put("context", context());
        final String page = TemplateCompiller.compileTemplate(viewName, data);
        return page;
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
