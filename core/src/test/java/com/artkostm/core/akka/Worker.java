package com.artkostm.core.akka;

import com.artkostm.core.akka.processor.ProcessorFacade;

import akka.actor.UntypedActor;

public class Worker extends UntypedActor
{
    @Override
    public void onReceive(final Object arg0) throws Exception 
    {
        ProcessorFacade.process(this, arg0);
    }
}
