package com.artkostm.core.akka.util.reaper;

import akka.actor.Props;

public class ProductionReaper extends Reaper
{
    @Override
    protected void allSoulsReaped() 
    {
        context().system().terminate();
    }
    
    public static Props props()
    {
        return Props.create(ProductionReaper.class);
    }
}
