package com.artkostm.core.controller;

import io.netty.handler.codec.http.cookie.Cookie;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.artkostm.core.controller.session.Session;
import com.artkostm.core.controller.session.SessionHandler;

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
    private Map<String, String> pathParams;
    //example: /books/23?format=pdf&edition=2 -> ["format":"pdf", "edition":"2"]
    private Map<String, List<String>> queryParams;
    private Map<String, List<Cookie>> cookies;
    private Map<String, Object> cookiesForClient;
    private byte[] content;
    private SessionHandler handler;
    
    
    public Context()
    {
        pathParams = new HashMap<String, String>();
        queryParams = new HashMap<String, List<String>>();
        cookies = new HashMap<String, List<Cookie>>();
        cookiesForClient = new HashMap<String, Object>();
        current.set(this);
    }
    
    public Context(final SessionHandler sessionHandler)
    {
        pathParams = new HashMap<String, String>();
        queryParams = new HashMap<String, List<String>>();
        cookies = new HashMap<String, List<Cookie>>();
        cookiesForClient = new HashMap<String, Object>();
        handler = sessionHandler;
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
        if (handler!= null && session == null) 
        {
            handler.process(this);
        }
        return session;
    }
    
    public void setSession(final Session session)
    {
        this.session = session;
    }

    public Map<String, String> getPathParams()
    {
        return pathParams;
    }

    public void setPathParams(final Map<String, String> pathParams)
    {
        this.pathParams = pathParams;
    }

    public Map<String, List<String>> getQueryParams()
    {
        return queryParams;
    }

    public void setQueryParams(final Map<String, List<String>> queryParams)
    {
        this.queryParams = queryParams;
    }

    public Map<String, List<Cookie>> getCookies()
    {
        return cookies;
    }

    public void setCookies(final Map<String, List<Cookie>> cookies)
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
    
    public byte[] getContent() 
    {
        return content;
    }

    public void setContent(final byte[] content) 
    {
        this.content = content;
    }

    @Override
    public String toString() 
    {
        return "Context [pathParams=" + pathParams
                + ", queryParams=" + queryParams + ", cookies=" + cookies
                + ", cookiesForClient=" + cookiesForClient + "]";
    }
}
