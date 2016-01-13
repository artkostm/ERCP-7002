package com.artkostm.core.akka.processor;

import akka.actor.Actor;
import akka.event.Logging;

import com.artkostm.core.akka.model.Job;
import com.artkostm.core.akka.model.JobResult;

public class JobProcessor implements Processor<Job>
{
    @Override
    public void process(final Actor actor, final Job job) 
    {
        //Logging.getLogger(actor.context().system(), actor).info("JobID " + job.getId());
        for (long i = job.getForm(); i < job.getTo(); i++)
        {
            if (job.getNumber() % i == 0)
            {
                final String message = String.format("[from=%s, to=%s]", job.getForm(), job.getTo());
                actor.sender().tell(new JobResult(job.getId(), false, message), actor.self());
                return;
            }
        }
        actor.sender().tell(new JobResult(job.getId(), true, String.valueOf("")), actor.self());
    }
}
