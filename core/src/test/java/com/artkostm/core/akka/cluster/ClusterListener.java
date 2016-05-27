package com.artkostm.core.akka.cluster;

import akka.actor.UntypedActor;
import akka.cluster.Cluster;
import akka.cluster.ClusterEvent;
import akka.cluster.ClusterEvent.MemberEvent;
import akka.cluster.ClusterEvent.MemberRemoved;
import akka.cluster.ClusterEvent.MemberUp;
import akka.cluster.ClusterEvent.UnreachableMember;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class ClusterListener extends UntypedActor
{
    final LoggingAdapter log = Logging.getLogger(getContext().system(), this);
    final Cluster cluster = Cluster.get(context().system());
    
    @Override
    public void preStart() throws Exception
    {
        cluster.subscribe(self(), ClusterEvent.initialStateAsEvents(),
            MemberEvent.class, UnreachableMember.class);
        super.preStart();
    }
    
    @Override
    public void onReceive(Object message) throws Exception
    {
        if (message instanceof MemberUp)
        {
            MemberUp mUp = (MemberUp) message;
            log.info("Member is Up: {}", mUp.member());

        }
        else if (message instanceof UnreachableMember)
        {
            UnreachableMember mUnreachable = (UnreachableMember) message;
            log.info("Member detected as unreachable: {}", mUnreachable.member());

        }
        else if (message instanceof MemberRemoved)
        {
            MemberRemoved mRemoved = (MemberRemoved) message;
            log.info("Member is Removed: {}", mRemoved.member());

        }
        else if (message instanceof MemberEvent)
        {
            
        }
        else
        {
            System.out.println("Received external message: " + message);
            unhandled(message);
        }
    }
    
    @Override
    public void postStop() throws Exception
    {
        cluster.unsubscribe(self());
        super.postStop();
    }
}
