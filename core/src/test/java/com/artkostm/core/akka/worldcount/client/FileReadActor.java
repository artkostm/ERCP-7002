package com.artkostm.core.akka.worldcount.client;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import akka.actor.AbstractActor;
import akka.japi.pf.ReceiveBuilder;

public class FileReadActor extends AbstractActor
{
    public FileReadActor()
    {
        receive(ReceiveBuilder.match(String.class, this::read) //file name
            .build());
    }
    
    private void read(final String filename)
    {
        final BufferedReader reader = new BufferedReader(
            new InputStreamReader(
                Thread.currentThread().getContextClassLoader().getResourceAsStream(filename)));
        
        try
        {
            String line = null;
            while ((line = reader.readLine()) != null) 
            {
                sender().tell(line, sender());
            }
            System.out.println("All lines send !");
            
            sender().tell(String.valueOf("EOF"), self());
        }
        catch (Exception e)
        {
            System.err.format("IOException: %s%n", e);
        }
    }
}
