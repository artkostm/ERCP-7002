package com.artkostm.core;

import java.lang.reflect.Method;
import java.util.Collections;

import com.artkostm.configurator.model.Metadata;

public class TestApp extends WebApplicationAdaptor
{
    @Override
    public Metadata configure()
    {
        router().GET("/", Collections.<Method>emptyList());
        return super.configure();
    }
    
    public static void main(String[] args)
    {
        new TestApp().run();
    }
}
