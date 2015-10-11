package com.artkostm.configurator.model;

import groovy.lang.Closure;

public interface Script 
{
    public void apply(Closure<?> closure);
}
