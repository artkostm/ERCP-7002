package com.artkostm.core.akka.util.throttler;

import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import com.artkostm.core.akka.http.HttpMethodRoutingPoolTest.HttpMethodTestActor;
import com.artkostm.core.akka.util.throttler.Data.Rate;

import scala.compat.java8.functionConverterImpls.FromJavaFunction;
import scala.concurrent.duration.Duration;
import akka.actor.AbstractFSM;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;

import static com.artkostm.core.akka.util.throttler.State.Idle;
import static com.artkostm.core.akka.util.throttler.State.Active;

public class Throttler extends AbstractFSM<State, Data>
{
    private Rate rate;
    
    {
        startWith(Idle, Data.None);
        
        when(Idle, matchEvent(String.class, Data.None.getClass(),
            (obj, none) ->
            {
                return null;
            }
        ));
        
        when(Active, matchEvent(String.class, Data.None.getClass(),
            (obj, none) ->
            {
                return null;
            }
        ));
        
        onTransition(matchState(
            Idle, Active, () -> setTimer("moreVouchers", Data.Tick, Duration.create(rate.durationInMillis(), TimeUnit.SECONDS), true))
            .state(Active, Idle, () -> cancelTimer("moreVouchers")));
        
        initialize();
    }
    
    public Throttler(Rate rate)
    {
        this.rate = rate;
    }
    
    @SuppressWarnings("unchecked")
    private void deliverMessages(final TimeBasedData d)
    {
        final int nrOfMsgToSend = min(d.getQueue().length(), d.getVouchersLeft());
        d.getQueue().take(nrOfMsgToSend).foreach(
                new FromJavaFunction<Object, Void>(new Function<Object, Void>() 
                {
                    @Override
                    public Void apply(Object msg) 
                    {
                        d.getTarget().tell(msg, ActorRef.noSender());
                        return VoidObj;
                    }
                }
        ));
        
        d.getQueue().drop(nrOfMsgToSend);
        d.setVouchersLeft(d.getVouchersLeft() - nrOfMsgToSend);
    }

    private int min(int length, int vouchersLeft) 
    {
        return 0;
    }
    
    public static Void VoidObj = null;
    
    public static void main(String[] args)
    {
        ActorSystem system = ActorSystem.create();
        ActorRef tActor = system.actorOf(Props.create(HttpMethodTestActor.class));
        TimeBasedData d = new TimeBasedData(tActor, 4);
    }
}
