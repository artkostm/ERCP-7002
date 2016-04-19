package com.artkostm.core.guice.module;

import java.net.InetSocketAddress;
import java.net.SocketAddress;

import akka.actor.ActorSystem;
import akka.actor.DeadLetter;

import com.artkostm.core.akka.actors.util.AuditLogActor;
import com.artkostm.core.akka.actors.util.ConsoleLogActor;
import com.artkostm.core.akka.configuration.RouteObject;
import com.artkostm.core.akka.configuration.RouterFactory;
import com.artkostm.core.akka.http.message.HttpMessage;
import com.artkostm.core.configuration.internal.AppConfig;
import com.artkostm.core.guice.annotation.Config.Host;
import com.artkostm.core.guice.annotation.Config.Port;
import com.artkostm.core.guice.annotation.Config.TemplateLoadingDir;
import com.artkostm.core.web.ApplicationConstants;
import com.artkostm.core.web.network.router.Router;
import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import com.typesafe.config.Config;

/**
 * 
 * @author Artsiom_Chuiko
 *
 */
public abstract class ConfigurationModule extends AbstractModule implements ApplicationConstants
{   
    @Override
    protected void configure()
    {
        final Integer port = port() == 0 ? DEFAULT_PORT : port();
        bindConstant().annotatedWith(Port.class).to(port);
        
        final String host = host() == null ? LOCAL_HOST : host();
        bindConstant().annotatedWith(Host.class).to(host);
        
        final String templateLoadingDir = templateLoadingDir() == null ? 
            DEFAULT_DIRECTORY_FOR_TEMPLATE_LOADING : templateLoadingDir();
        bindConstant().annotatedWith(TemplateLoadingDir.class).to(templateLoadingDir);
        
        bind(SocketAddress.class).toInstance(new InetSocketAddress(host, port));
        
        final ActorSystem system = ActorSystem.create("server", config());
        bind(ActorSystem.class).toInstance(system);
        system.eventStream().subscribe(system.actorOf(AuditLogActor.props()), HttpMessage.class);
        system.eventStream().subscribe(system.actorOf(ConsoleLogActor.props()), DeadLetter.class);
        
        final RouterFactory factory = new RouterFactory(system);
        final Router<RouteObject> router = factory.get(config());
        bind(new TypeLiteral<Router<RouteObject>>() {}).toInstance(router);
    }
    
    protected abstract Integer port();
    
    protected abstract Config config();
    
    protected abstract String host();
    
    protected abstract String templateLoadingDir();
    
    protected abstract AppConfig applicationConfiguration();
}
