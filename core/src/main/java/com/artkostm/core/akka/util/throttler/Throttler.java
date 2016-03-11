package com.artkostm.core.akka.util.throttler;

import java.util.concurrent.TimeUnit;

import com.artkostm.core.akka.util.throttler.Data.Rate;

import scala.concurrent.duration.Duration;
import akka.actor.AbstractFSM;
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
}
