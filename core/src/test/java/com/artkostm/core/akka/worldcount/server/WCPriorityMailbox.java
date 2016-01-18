package com.artkostm.core.akka.worldcount.server;

import com.typesafe.config.Config;

import akka.actor.ActorSystem;
import akka.actor.PoisonPill;
import akka.dispatch.PriorityGenerator;
import akka.dispatch.UnboundedPriorityMailbox;

public class WCPriorityMailbox extends UnboundedPriorityMailbox
{

    public WCPriorityMailbox(final ActorSystem.Settings settings, final Config config)
    {
        super(generator);
    }
    
    private static final PriorityGenerator generator = new PriorityGenerator()
    {
        @Override
        public int gen(Object msg)
        {
            if ("DISPLAY_LIST".equals(msg))
            {
                return 2;
            }
            else if (msg.equals(PoisonPill.getInstance()))
            {
                return 1;
            }
            return 0;
        }
    };
}
