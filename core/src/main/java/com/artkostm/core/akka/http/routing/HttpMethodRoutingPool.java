package com.artkostm.core.akka.http.routing;

import java.util.concurrent.TimeUnit;

import com.artkostm.core.akka.http.message.HttpMessage;
import com.typesafe.config.Config;

import akka.actor.ActorSystem;
import akka.actor.OneForOneStrategy;
import akka.actor.SupervisorStrategy;
import akka.dispatch.Dispatchers;
import akka.japi.pf.DeciderBuilder;
import akka.routing.DefaultOptimalSizeExploringResizer;
import akka.routing.PoolBase;
import akka.routing.Resizer;
import akka.routing.Router;
import scala.Option;
import scala.concurrent.duration.Duration;

public class HttpMethodRoutingPool extends PoolBase
{
    private static final long serialVersionUID = -8668444273017076975L;
    
    private final int nrOfInstances;
    
    public HttpMethodRoutingPool(final int nrOfInstances) 
    {
        this.nrOfInstances = nrOfInstances;
    }
    
    public HttpMethodRoutingPool(final Config config) 
    {
        this.nrOfInstances = config.getInt("nr-of-instances");
    }

    @Override
    public Router createRouter(ActorSystem system)
    {
        return new Router(new HttpMethodRoutingLogic());
    }

    @Override
    public int nrOfInstances(ActorSystem system) 
    {
        return nrOfInstances;
    }

    @Override
    public Option<Resizer> resizer() 
    {
        return Option.apply(new DefaultOptimalSizeExploringResizer(
                2,                                    //lowerBound: PoolSize = 1,
                18,                                   //upperBound: PoolSize = 30,
                0.2,                                  //chanceOfScalingDownWhenFull: Double = 0.2,
                Duration.create(5, TimeUnit.SECONDS), //actionInterval: Duration = 5.seconds,
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
        return new OneForOneStrategy(false, DeciderBuilder.match(Throwable.class, e -> SupervisorStrategy.resume()).build());
    }

    @Override
    public String routerDispatcher() 
    {
        return Dispatchers.DefaultDispatcherId();
    }

    @Override
    public boolean isManagementMessage(Object msg) 
    {
        return super.isManagementMessage(msg) || !(msg instanceof HttpMessage);
    }
}