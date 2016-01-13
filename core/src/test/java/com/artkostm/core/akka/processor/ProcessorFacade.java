package com.artkostm.core.akka.processor;

import java.util.HashMap;
import java.util.Map;

import com.artkostm.core.akka.model.Job;
import com.artkostm.core.akka.model.JobResult;

import akka.actor.Actor;

public class ProcessorFacade
{
    private static final Map<Class<?>, Processor<?>> processors;
    
    private static final DefaultProcessor defaultProcessor = new DefaultProcessor();
    
    static
    {
        processors = new HashMap<Class<?>, Processor<?>>();
        processors.put(String.class, new StringProcessor());
        processors.put(JobResult.class, new JobResultProcessor());
        processors.put(Job.class, new JobProcessor());
    }
    
    @SuppressWarnings("unchecked")
    public static void process(final Actor actor, final Object obj) 
    {
        final Processor<Object> processor = (Processor<Object>) processors.get(obj.getClass());
        if (processor != null)
        {
            processor.process(actor, obj);
        }
        else
        {
            defaultProcessor.process(actor, obj);
        }
    }
}
