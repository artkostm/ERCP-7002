package com.artkostm.core;

import com.artkostm.configurator.model.Metadata;

public interface Application extends Runnable
{
    void configure(Metadata config);
}
