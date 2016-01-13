package com.artkostm.core.akka;

import com.artkostm.core.akka.processor.ProcessorFacade;

import akka.actor.UntypedActor;

public class Kernel extends UntypedActor
{
    @Override
    public void onReceive(Object arg0) throws Exception 
    {
        ProcessorFacade.process(this, arg0);
    }
}
