package com.artkostm.core.akka.processor;

import java.util.concurrent.Callable;

import scala.concurrent.Future;
import akka.actor.Actor;
import akka.dispatch.Futures;
import akka.dispatch.OnComplete;
import akka.pattern.Patterns;

import com.artkostm.core.akka.model.Job;
import com.artkostm.core.akka.model.JobResult;

public class JobProcessor implements Processor<Job>
{
    @Override
    public void process(final Actor actor, final Job job) 
    {        
        final Future<JobResult> result = Futures.future(new Callable<JobResult>()
        {
            @Override
            public JobResult call() throws Exception
            {
                return check(job);
            }
        }, actor.context().dispatcher());
        
        result.onComplete(onComplete, actor.context().dispatcher());
        
        Patterns.pipe(result, actor.context().dispatcher()).to(actor.sender());
    }
    
    protected JobResult check(final Job job)
    {
        for (long i = job.getForm(); i < job.getTo(); i++)
        {
            if (job.getNumber() % i == 0)
            {
                final String message = String.format("[from=%s, to=%s]", job.getForm(), job.getTo());
                return new JobResult(job.getId(), false, message);
            }
        }
        return new JobResult(job.getId(), true, String.valueOf(""));
    }
    
    private static final OnComplete<JobResult> onComplete = new OnComplete<JobResult>()
    {
        @Override
        public void onComplete(Throwable arg0, JobResult arg1) throws Throwable
        {
            //if (arg1 != null) System.out.println("Job#" + arg1.getId() + " is completed.");
            if (arg0 != null) arg0.printStackTrace();
        }
    };
}
