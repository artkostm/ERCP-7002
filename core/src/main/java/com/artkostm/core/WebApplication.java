package com.artkostm.core;

import com.artkostm.configurator.model.Metadata;

public abstract class WebApplication implements Application
{
    @Override
    public void run()
    {
        System.out.println(Thread.currentThread().getName()+":getConfiguration");
        final Metadata config = new Metadata()
        {
            @Override
            public Object getData()
            {
                return Thread.currentThread().getName()+"HelloData";
            }
        };
        System.out.println(Thread.currentThread().getName()+":configure");
        configure(config);
        System.out.println(Thread.currentThread().getName()+":startApp");
    }
}
