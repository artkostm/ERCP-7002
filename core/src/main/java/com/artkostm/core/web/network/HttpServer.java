package com.artkostm.core.web.network;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.SelfSignedCertificate;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;

import java.net.SocketAddress;

import com.artkostm.core.guice.annotation.Config.Ssl;
import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * 
 * @author Artsiom_Chuiko
 *
 */
@Singleton
public class HttpServer implements Runnable
{
    private static final InternalLogger LOG = InternalLoggerFactory.getInstance(HttpServer.class);
    
    @Ssl
    private boolean SSL;
    
    @Inject
    private SocketAddress address;
    
    @Inject
    private HttpServerInitializer initializer;
    
    @Override
    public void run()
    {
        SslContext sslCtx = null;
        if (SSL) 
        {
            try
            {
                SelfSignedCertificate ssc = new SelfSignedCertificate();
                sslCtx = SslContextBuilder.forServer(ssc.certificate(), ssc.privateKey()).build();
                initializer.setSslContext(sslCtx);
            }
            catch (Exception e)
            {
                LOG.warn("Cannot instantiate SSL context", e);
            }
        }
        
        final EventLoopGroup bossGroup = new NioEventLoopGroup();
        final EventLoopGroup workerGroup = new NioEventLoopGroup();
        try 
        {
            final ServerBootstrap b = new ServerBootstrap();
            b.option(ChannelOption.SO_BACKLOG, 1024);
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    //.handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(initializer);
            final Channel ch = b.bind(address).sync().channel();
            ch.closeFuture().sync();
        }
        catch (InterruptedException e)
        {
            LOG.warn("Cannot create server bootstrap or new channel", e);
        } 
        finally 
        {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
