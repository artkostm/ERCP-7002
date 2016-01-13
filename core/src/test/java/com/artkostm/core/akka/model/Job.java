package com.artkostm.core.akka.model;

import java.io.Serializable;

public class Job implements Serializable
{
    /** * */
    private static final long serialVersionUID = 1L;
    
    private final long form;
    private final long to;
    private final long number;
    
    private final long id;
    
    public Job(long number, long form, long to, long id) 
    {
        super();
        this.form = form;
        this.to = to;
        this.id = id;
        this.number = number;
    }

    public long getForm() 
    {
        return form;
    }

    public long getTo() 
    {
        return to;
    }

    public long getNumber() 
    {
        return number;
    }

    public long getId() 
    {
        return id;
    }    
}
