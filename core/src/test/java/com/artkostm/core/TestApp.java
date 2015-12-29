package com.artkostm.core;

public class TestApp extends WebApplicationAdapter
{     
    private static final String CONFIG_PATH = "classpath:configuration.gr";
    
    public static void main(String[] args)
    {
        ApplicationBootstrap.run(TestApp.class, CONFIG_PATH);
    }
}
