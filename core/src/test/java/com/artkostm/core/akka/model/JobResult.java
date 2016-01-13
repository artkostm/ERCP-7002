package com.artkostm.core.akka.model;

import java.io.Serializable;

public class JobResult implements Serializable
{
    /** * */
    private static final long serialVersionUID = 1L;
    
    private final long id;
    private final boolean isPrime;
    private final String message;
    
    public JobResult(final long id, final boolean isPrime, final String msg) 
    {
        super();
        this.id = id;
        this.isPrime = isPrime;
        message = msg;
    }

    public long getId() 
    {
        return id;
    }

    public boolean isPrime() 
    {
        return isPrime;
    }
    
    public String getMessage()
    {
        return message;
    }
}
