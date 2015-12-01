package com.artkostm.core.controller;

import java.util.HashMap;
import java.util.Map;

import com.artkostm.core.controller.session.Session;

public class Context 
{
    private static ThreadLocal<Context> current = new ThreadLocal<Context>();

    /**
     * Retrieves the current HTTP context, for the current thread.
     *
     * @return the context
     */
    public static Context current() 
    {
        final Context c = current.get();
        if(c == null) 
        {
            throw new RuntimeException("There is no HTTP Context available from here.");
        }
        return c;
    }
    
    private Session session;
    //example: /books/:id = /books/23 -> pathParams = ["id":"23"]
    private Map<String, Object> pathParams;
    //example: /books/23?format=pdf&edition=2 -> ["format":"pdf", "edition":"2"]
    private Map<String, Object> queryParams;
    private Map<String, Object> cookies;
    private Map<String, Object> cookiesForClient;
    
    
    public Context()
    {
        pathParams = new HashMap<String, Object>();
        queryParams = new HashMap<String, Object>();
        cookies = new HashMap<String, Object>();
        cookiesForClient = new HashMap<String, Object>();
        current.set(this);
    }
    
    public void addCookie(final String name, final Object value)
    {
        cookiesForClient.put(name, value);
    }
    
    public Object getCookie(final String name)
    {
        return cookiesForClient.get(name);
    }
    
    public void removeCookie(final String name)
    {
        cookiesForClient.remove(name);
    }

    public Session getSession()
    {
        if (session == null) 
        {
            //getSessionManager().process(this);
        }
        return session;
    }
    
    public void setSession(final Session session)
    {
        this.session = session;
    }

    public Map<String, Object> getPathParams()
    {
        return pathParams;
    }

    public void setPathParams(final Map<String, Object> pathParams)
    {
        this.pathParams = pathParams;
    }

    public Map<String, Object> getQueryParams()
    {
        return queryParams;
    }

    public void setQueryParams(final Map<String, Object> queryParams)
    {
        this.queryParams = queryParams;
    }

    public Map<String, Object> getCookies()
    {
        return cookies;
    }

    public void setCookies(final Map<String, Object> cookies)
    {
        this.cookies = cookies;
    }

    public Map<String, Object> getCookiesForClient()
    {
        return cookiesForClient;
    }

    public void setCookiesForClient(final Map<String, Object> cookiesForClient)
    {
        this.cookiesForClient = cookiesForClient;
    }
}
