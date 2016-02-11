package com.artkostm.core.akka.extension.netty;

import akka.actor.AbstractExtensionId;
import akka.actor.ExtendedActorSystem;
import akka.actor.Extension;
import akka.actor.ExtensionId;
import akka.actor.ExtensionIdProvider;

public class NettyProvider extends AbstractExtensionId<NettyServerExtension> implements ExtensionIdProvider
{
    public static final NettyProvider Netty = new NettyProvider(); 
    
    @Override
    public NettyServerExtension createExtension(ExtendedActorSystem system)
    {
        return new NettyServerExtension(system.settings().config());
    }

    @Override
    public ExtensionId<? extends Extension> lookup()
    {
        return Netty;
    }
}
