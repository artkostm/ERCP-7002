package com.artkostm.core;

import com.artkostm.configurator.model.Metadata;

public class WebApplicationAdaptor extends WebApplication
{
    @Override
    public Metadata configure() 
    {
        return EMPTY_METADATA;
    }
}
