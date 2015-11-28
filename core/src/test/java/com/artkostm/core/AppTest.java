package com.artkostm.core;

import java.lang.reflect.Modifier;

import com.artkostm.configurator.model.Metadata;

public class AppTest extends WebApplication
{
    public static void main(String[] args)
    {
        new Thread(new AppTest()).start();
        System.out.println(Thread.currentThread().getName()+":End of main(...)");
    }

    @Override
    public void configure(Metadata config)
    {
        System.out.println(Thread.currentThread().getName()+":Metadata.getData()->"+config.getRouteConfigList());
        try
        {
            System.out.println(Modifier.isStatic(AppTest.class.getDeclaredMethod("main", new Class<?>[]{String[].class}).getModifiers()));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
