package com.artkostm.core.web;

public class WebApplicationAdapter extends WebApplication
{
    @Override
    public void configure() 
    {
        System.out.println(router());
    }
}
