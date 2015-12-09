package com.artkostm.core;

import com.artkostm.configurator.model.Metadata;

public class TestApp extends WebApplication
{
    @Override
    public Metadata configure()
    {
        return configurator().createConfiguration("classpath:configuration.gr");
    }
    
    public static void main(String[] args)
    {
        new TestApp().run();
    }
}
