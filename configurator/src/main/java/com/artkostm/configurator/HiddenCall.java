package com.artkostm.configurator;

public interface HiddenCall
{
    public String name();
    
    Object doCall(Object...args);
}
