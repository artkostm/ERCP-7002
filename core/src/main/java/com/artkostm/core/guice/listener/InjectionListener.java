package com.artkostm.core.guice.listener;

import com.google.inject.TypeLiteral;
import com.google.inject.spi.TypeEncounter;
import com.google.inject.spi.TypeListener;

public class InjectionListener implements TypeListener
{
    @Override
    public <I> void hear(TypeLiteral<I> type, TypeEncounter<I> encounter)
    {
        
    }
}
