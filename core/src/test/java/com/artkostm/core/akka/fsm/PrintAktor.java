package com.artkostm.core.akka.fsm;

import akka.actor.Props;
import akka.actor.UntypedActor;

public class PrintAktor extends UntypedActor
{
    @Override
    public void onReceive(Object arg0) throws Exception
    {
        for (char c : String.valueOf(arg0).toCharArray())
        {
            Thread.sleep(200);
            System.out.print(c + " ");
        }
        sender().tell(String.valueOf(arg0), self());
    }
    
    public static Props props()
    {
        return Props.create(PrintAktor.class);
    }
}
