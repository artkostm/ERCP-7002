package com.artkostm.core.web;

/**
 * 
 * @author Artsiom_Chuiko
 *
 */
public class WebApplicationAdapter extends WebApplication
{
    @Override
    public void configure() 
    {
        System.out.println(router());
    }
}
