package com.artkostm.core.akka.camel.http;

import java.util.HashMap;

import com.artkostm.core.akka.http.message.AppInternalMessage;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.camel.CamelMessage;
import akka.camel.javaapi.UntypedConsumerActor;
import akka.camel.javaapi.UntypedProducerActor;

public class Main
{
    public static void main(String[] args)
    {
        final ActorSystem system = ActorSystem.create();
        final ActorRef producer = system.actorOf(Props.create(Producer.class));
        system.actorOf(Props.create(Consumer.class, producer)).tell(null, ActorRef.noSender());
        //producer.tell("stringa", ActorRef.noSender());
    }
    
    public static class Consumer extends UntypedConsumerActor
    {
        ActorRef producer;
        
        public Consumer(ActorRef producer)
        {
            this.producer = producer;
        }
        
        @Override
        public String getEndpointUri()
        {
            return "direct:start";
        }

        @Override
        public void onReceive(Object arg0) throws Exception
        {
            System.out.println(arg0);
            producer.forward(arg0, getContext());
        }
    }
    
    public static class Producer extends UntypedProducerActor
    {
        @Override
        public String getEndpointUri()
        {
            return "http://camel.apache.org/jetty.html";
        }
        
        @Override
        public Object onTransformOutgoingMessage(Object message)
        {
            System.out.println("onTransformOutgoingMessage: " + message);
            return super.onTransformOutgoingMessage(message);
        }
        
        @Override
        public Object onTransformResponse(Object message)
        {
            System.out.println("onTransformResponse: " + message);
            return super.onTransformResponse(message);
        } 
        
        @Override
        public void onRouteResponse(Object message)
        {
            System.out.println("Msg: " + message);
            System.out.println(((CamelMessage)message).getBodyAs(String.class, camelContext()));
            sender().tell(new AppInternalMessage(null, null, message), self());
        }
    }
}
