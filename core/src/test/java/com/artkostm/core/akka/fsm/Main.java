package com.artkostm.core.akka.fsm;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import scala.concurrent.duration.Duration;
import akka.actor.AbstractLoggingFSM;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import static com.artkostm.core.akka.fsm.Unauthorized.Unauthorized;
import static com.artkostm.core.akka.fsm.FSMState.Active;
import static com.artkostm.core.akka.fsm.FSMState.Idle;

public class Main extends AbstractLoggingFSM<State, Data>
{
    {
        startWith(Idle, Unauthorized);
        
        when(Idle, matchEvent(String.class, com.artkostm.core.akka.fsm.Unauthorized.class, 
            (obj, unauthorized) -> 
            {
                System.out.println("when(Idle, unauthorized, " + obj + ")");
                return goTo(Active).using(new StringData(obj));
            }
        ));
        
        when(Active, Duration.create(7, TimeUnit.SECONDS),
            matchEvent(Arrays.asList(Flush.class, StateTimeout()), StringData.class,
                (event, data) -> 
                {
                    System.out.println("when(Active, " + data + ", " + event +  ")");
                    return goTo(Idle).using(data);
                }
        ));
        
        onTransition(matchState(Active, Idle,
            () -> { /*setTimer("timeout", "timeoutStr",  Duration.create(200, TimeUnit.MILLISECONDS), true);*/ System.out.println("Active -> Idle"); })
            //.state(Active, null, (f, t) -> { cancelTimer("timeout"); log().info("from Active to " + t); })
            .state(Idle, null, (f, t) -> { System.out.println("Idle -> " + t); cancelTimer("timeout");}));
        
        whenUnhandled(
            matchEvent(Arrays.asList(String.class, Object.class, int.class), 
                (x, data) -> 
                {
                    System.out.println("Received unhandled event: " + x);
                    return goTo(Active).using(new StringData(x));
                }
            ).anyEvent(
                (event, data) -> 
                 {
                     System.out.println("Received unknown event: " + event);
                     return goTo(Idle);
                 }
         ));


        
        initialize();
    }
    
    public static void main(String[] args) throws InterruptedException
    {
        final ActorSystem system = ActorSystem.create("custom_system");
        final ActorRef fsm = system.actorOf(Props.create(Main.class));
        final ActorRef print = system.actorOf(PrintAktor.props());
        
        fsm.tell("string", print);
        Thread.sleep(2000);
//        fsm.tell(Flush.Flush, print);
        fsm.tell(Flush.Flush, print);
        Thread.sleep(2000);
        fsm.tell("string2", print);
//        Thread.sleep(1000);
//        fsm.tell(123, print);
//        fsm.tell(int.class, print);
//        Thread.sleep(1000);
//        fsm.tell(Flush.Flush, print);
//        Thread.sleep(100);
//        fsm.tell("Hello, World!", print);
        Thread.sleep(20000);
        system.stop(fsm);
        system.terminate();
    }
    
    public static class StringData implements Data
    {
        private final String data;
        
        public StringData(Object obj)
        {
            data = String.valueOf(obj);
        }

        public String getData()
        {
            return data;
        }
        
        @Override
        public String toString()
        {
            return "Data: " + data.toString();
        }
    }
    
    public static enum Flush 
    {
        Flush;
    }
}
