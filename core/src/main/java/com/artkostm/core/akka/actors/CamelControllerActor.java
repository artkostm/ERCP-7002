package com.artkostm.core.akka.actors;

import java.util.HashMap;

import com.artkostm.core.akka.http.message.HttpMessage;
import com.artkostm.core.web.controller.Result;

import akka.actor.ActorRef;
import akka.actor.PoisonPill;
import akka.actor.Props;
import akka.actor.Status;
import akka.camel.CamelMessage;
import akka.camel.javaapi.UntypedConsumerActor;
import akka.camel.javaapi.UntypedProducerActor;
import akka.japi.Creator;

public abstract class CamelControllerActor extends ControllerActor
{
    @Override
    public void onReceive(Object msg) throws Exception
    {
        if (msg instanceof HttpMessage)
        {
            final ActorRef producer = context().system().actorOf(Props.create(new ProducerCreator(this, (HttpMessage) msg)));
            context().system().actorOf(Props.create(new ConsumerCreator(this, producer)))
            .tell(new CamelMessage("", new HashMap<>()), self());
        }
        else 
        {
            unhandled(msg);
        }
    }
    
    protected abstract Result onRequest(HttpMessage msg) throws Exception;
    protected abstract String getProducerUri();
    protected abstract String getConsumerUri();
    
    protected final class Producer extends UntypedProducerActor
    {
        private final HttpMessage httpMessage;
        
        public Producer(final HttpMessage httpMessage)
        {
            this.httpMessage = httpMessage;
        }
        
        public void onRouteResponse(Object message) 
        {
            if (message instanceof CamelMessage)
            {
                httpMessage.payload(((CamelMessage) message).getBodyAs(String.class, camelContext()));
                Result result = null;
                try
                {
                    result = onRequest(httpMessage);
                }
                catch (Exception e)
                {
                    result = internalServerError(e);
                }
                mapResult(result, httpMessage);
            }
            if (message instanceof Status.Failure)
            {
                mapResult(internalServerError(((Status.Failure) message).cause()), httpMessage);
            }
            self().tell(PoisonPill.getInstance(), ActorRef.noSender());
        }
        
        @Override
        public String getEndpointUri()
        {
            return getProducerUri();
        }
    };
    
    protected final class Consumer extends UntypedConsumerActor
    {
        private final  ActorRef producer;
        
        public Consumer(final  ActorRef producer)
        {
            this.producer = producer;
        }
        
        @Override
        public String getEndpointUri()
        {
            return getConsumerUri();
        }

        @Override
        public void onReceive(Object msg) throws Exception
        {
            producer.forward(msg, context());
            self().tell(PoisonPill.getInstance(), ActorRef.noSender());
        }
    }
    
    public static class ProducerCreator implements Creator<Producer>
    {
        private static final long serialVersionUID = 607162541682659694L;
        
        private final CamelControllerActor controller;
        private final HttpMessage httpMessage;
        
        public ProducerCreator(final CamelControllerActor controller, final HttpMessage httpMessage)
        {
            this.controller = controller;
            this.httpMessage = httpMessage;
        }
        
        @Override
        public Producer create() throws Exception
        {
            return controller.new Producer(httpMessage);
        }
    }
    
    public static class ConsumerCreator implements Creator<Consumer>
    {
        private static final long serialVersionUID = 607162541682659694L;
        
        private final CamelControllerActor controller;
        private final  ActorRef producer;
        
        public ConsumerCreator(final CamelControllerActor controller, final  ActorRef producer)
        {
            this.controller = controller;
            this.producer = producer;
        }
        
        @Override
        public Consumer create() throws Exception
        {
            return controller.new Consumer(producer);
        }
    }
}
