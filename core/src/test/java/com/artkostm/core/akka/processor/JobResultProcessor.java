package com.artkostm.core.akka.processor;

import akka.actor.Actor;
import akka.actor.PoisonPill;
import akka.event.Logging;

import com.artkostm.core.akka.model.JobResult;

public class JobResultProcessor implements Processor<JobResult>
{
    @Override
    public void process(final Actor actor, final JobResult result) 
    {
        if (!result.isPrime())
        {
            Logging.getLogger(actor.context().system(), actor)
                .info("Job ID: " + result.getId() + ", job finished with non-prime result "  + result.getMessage());
        }
        actor.sender().tell(PoisonPill.getInstance(), actor.self());
    }
}
