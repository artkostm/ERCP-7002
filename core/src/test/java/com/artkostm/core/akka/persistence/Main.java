package com.artkostm.core.akka.persistence;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

import com.typesafe.config.ConfigFactory;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.japi.pf.ReceiveBuilder;
import akka.persistence.DeleteMessagesFailure;
import akka.persistence.DeleteMessagesSuccess;
import akka.persistence.Persistence;
import akka.persistence.RecoveryCompleted;
import akka.persistence.UntypedPersistentActor;

public class Main
{
    public static void main(String[] args)
    {
        final ActorSystem system = ActorSystem.create("sys", ConfigFactory.parseString(
            "akka.persistence.journal.plugin = akka.persistence.journal.leveldb\n"
            + " akka.persistence.snapshot-store.plugin = akka.persistence.snapshot-store.local\n"
            + " akka.persistence.journal.leveldb.dir = target/example/journal\n"
            + " akka.persistence.snapshot-store.local.dir = target/example/snapshots\n"
            + " akka.persistence.journal.leveldb.native = false"));
        final ActorRef manager = system.actorOf(Props.create(Manager.class));
        manager.tell("soobcheniye19", ActorRef.noSender());
        manager.tell("soobcheniye29", ActorRef.noSender());
        manager.tell("soobcheniye39", ActorRef.noSender());
        manager.tell("soobcheniye49", ActorRef.noSender());
    }
    
    public static class Manager extends AbstractActor
    {
        private final ActorRef persisted = context().actorOf(Props.create(Persisted.class));
        
        public Manager()
        {
            System.out.println(Persistence.get(context().system()).persistenceId(persisted));
            receive(ReceiveBuilder.match(String.class, msg ->
            {
                System.out.println("Manager: " + msg);
                if (!((String) msg).startsWith("event")) persisted.tell(msg, self());
            }).build());
        }
    }
    
    public static class Persisted extends UntypedPersistentActor
    {
        FileWriter writer;
        
        public Persisted()
        {
            try
            {
                writer = new FileWriter(new File("log-file.log"));
            }
            catch (IOException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        
        @Override
        public String persistenceId()
        {
            return self().path().name();
        }

        @Override
        public void onReceiveCommand(Object msg) throws Exception
        {
            if (msg instanceof DeleteMessagesSuccess)
            {
                return;
            }
            if (msg instanceof DeleteMessagesFailure)
            {
                System.err.println(((DeleteMessagesFailure) msg).cause());
                
            }
            //persistAsync(String.format("event-%s-body:{%s}", new Date().toString(), msg), event -> sender().tell(event, self()));
            //deleteMessages(lastSequenceNr());
        }

        @Override
        public void onReceiveRecover(Object msg) throws Exception
        {
            System.out.println("OnReceiveRecover: " + msg);
            if (msg instanceof String) writer.append(((String) msg).replaceAll("\r\n", " ") + "\n");
            if (msg instanceof RecoveryCompleted) 
            {
                writer.flush();
                writer.close();
            }
            //deleteMessages(lastSequenceNr());
        }
    }
}
