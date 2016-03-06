package com.artkostm.core.akka.http.routing;

import java.util.concurrent.TimeUnit;

import akka.actor.ActorSystem;
import akka.actor.SupervisorStrategy;
import akka.dispatch.Dispatchers;
import akka.routing.DefaultOptimalSizeExploringResizer;
import akka.routing.PoolBase;
import akka.routing.Resizer;
import akka.routing.Router;
import scala.Option;
import scala.concurrent.duration.Duration;

public class HttpMethodRoutingPool extends PoolBase
{
    private static final long serialVersionUID = -8668444273017076975L;

    @Override
    public Router createRouter(ActorSystem system) 
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int nrOfInstances(ActorSystem arg0) 
    {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public Option<Resizer> resizer() 
    {
        return Option.apply(new DefaultOptimalSizeExploringResizer(
                2,                                    //lowerBound: PoolSize = 1,
                8,                                    //upperBound: PoolSize = 30,
                0.2,                                  //chanceOfScalingDownWhenFull: Double = 0.2,
                Duration.create(5, TimeUnit.SECONDS), // actionInterval: Duration = 5.seconds,
                16,                                   //numOfAdjacentSizesToConsiderDuringOptimization: Int = 16,
                0.1,                                  //exploreStepSize: Double = 0.1,
                0.8,                                  //downsizeRatio: Double = 0.8,
                Duration.create(72, TimeUnit.HOURS),  //downsizeAfterUnderutilizedFor: Duration = 72.hours,
                0.4,                                  //explorationProbability: Double = 0.4,
                0.5));                                //weightOfLatestMetric: Double = 0.5
    }

    @Override
    public SupervisorStrategy supervisorStrategy() 
    {
        return SupervisorStrategy.defaultStrategy(); //TODO: create custom strategy
    }

    @Override
    public String routerDispatcher() 
    {
        return Dispatchers.DefaultDispatcherId();
    }
}
