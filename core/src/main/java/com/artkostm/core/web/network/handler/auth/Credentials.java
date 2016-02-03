package com.artkostm.core.web.network.handler.auth;

public class Credentials
{
    private final String auth;
    
    public Credentials(final String auth)
    {
        this.auth = auth;
    }

    public String getAuth()
    {
        return auth;
    }
}
