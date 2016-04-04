package com.artkostm.core.akka.supervision;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.OneForOneStrategy;
import akka.actor.Props;
import akka.actor.SupervisorStrategy;
import akka.actor.UntypedActor;
import akka.event.EventStream;
import akka.japi.pf.DeciderBuilder;
import akka.routing.SmallestMailboxPool;

public class Main
{
    public static void main(String[] args)
    {
        final ActorSystem system = ActorSystem.create();
        final ActorRef server = system.actorOf(Props.create(PrintServer.class));
        final ActorRef monitor = system.actorOf(Props.create(DeadLetterMonitor.class));
        
        system.eventStream().subscribe(monitor, Message.class);
        
        server.tell("String", ActorRef.noSender());
        server.tell(121, ActorRef.noSender());
        server.tell(434.4, ActorRef.noSender());
        server.tell(new Object(), ActorRef.noSender());
        server.tell(new Message("payload"), ActorRef.noSender());
        server.tell(server, ActorRef.noSender());
    }
    
    public static class PrintServer extends UntypedActor
    {
        private final ActorRef printerRouter = context().actorOf(Props.create(BrokenPrinter.class).withRouter(new SmallestMailboxPool(5)));
        
        private final EventStream eventStream = context().system().eventStream();
        
        @Override
        public SupervisorStrategy supervisorStrategy()
        {
            return new OneForOneStrategy(DeciderBuilder
                .match(RuntimeException.class, ex -> SupervisorStrategy.resume())
                .build());
        }
        
        @Override
        public void onReceive(Object arg0) throws Exception
        {
            if (arg0 instanceof Message) 
            {
                eventStream.publish(arg0);
                printerRouter.tell(arg0.toString(), self());
            }
            else printerRouter.tell(arg0, self());
        }
        
    }
    
    public static class BrokenPrinter extends UntypedActor
    {
        @Override
        public void onReceive(Object msg) throws Exception
        {
            if (!(msg instanceof String))
            {
                throw new RuntimeException("can't print it!");
            }
            
            System.out.println(msg);
        }
    }
    
    public static class DeadLetterMonitor extends UntypedActor
    {
        @Override
        public void onReceive(Object msg) throws Exception
        {
            System.out.println("Monitor: " + msg);
        }
    }
    
    public static class Message
    {
        private final String payload;
        
        public Message(final String payload)
        {
            this.payload = payload + "#" +System.currentTimeMillis();
        }

        @Override
        public String toString()
        {
            return "Message [payload=" + payload + "]";
        }
    }
}
