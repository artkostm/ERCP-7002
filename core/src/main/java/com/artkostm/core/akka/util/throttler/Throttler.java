package com.artkostm.core.akka.util.throttler;

import java.util.concurrent.TimeUnit;
import java.util.function.Function;

//import com.artkostm.core.akka.http.HttpMethodRoutingPoolTest.HttpMethodTestActor;
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

}
