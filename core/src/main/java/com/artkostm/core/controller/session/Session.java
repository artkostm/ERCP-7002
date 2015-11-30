package com.artkostm.core.controller.session;

import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

public class Session extends HashMap<String, Object>
{

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private long updateTime;
    private int timeout = 30; // 30 minutes
    private final UUID sessionId;

    public Session(final UUID sessionId) 
    {
        updateTime = new Date().getTime();
        this.sessionId = sessionId;
    }

    public long getUpdateTime() 
    {
        return updateTime;
    }

    public void update() 
    {
        updateTime = new Date().getTime();
    }

    public long getTimeout() 
    {
        return timeout;
    }

    public void setTimeout(final int timeout) 
    {
        this.timeout = timeout;
    }

    public String getString(final String name) 
    {
        return (String) this.get(name);
    }

    public Integer getInteger(final String name) 
    {
        return (Integer) this.get(name);
    }

    public Long getLong(final String name) 
    {
        return (Long) this.get(name);
    }

    public Boolean getBoolean(final String name) 
    {
        return (Boolean) this.get(name);
    }

    public Boolean test(final String name, final Object value) 
    {
        Object ivalue;
        if ((ivalue = this.get(name)) == null) 
        {
            return false;
        }
        if (!ivalue.getClass().equals(value.getClass())) 
        {
            return false;
        }
        return ivalue.equals(value);
    }

    public UUID getSessionId() 
    {
        return sessionId;
    }
}
