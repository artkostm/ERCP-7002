package com.artkostm.core.network;

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

import java.lang.reflect.Method;
import java.net.SocketAddress;
import java.util.List;

import com.artkostm.core.controller.session.SessionService;
import com.artkostm.core.network.router.Router;

public class HttpServer implements Runnable
{
    private static final InternalLogger LOG = InternalLoggerFactory.getInstance(HttpServer.class);
    
    static final boolean SSL = System.getProperty("ssl") != null;
    
    private final SocketAddress address;
    private final Router<List<Method>> router;
    private final SessionService sessionService;
    
    public HttpServer(final SocketAddress addr, final Router<List<Method>> router, final SessionService sessionService)
    {
        address = addr;
        this.router = router;
        this.sessionService = sessionService;
        LOG.info("\n" + router.toString());
    }
    
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
            }
            catch (Exception e)
            {
                LOG.warn("Cannot instantient SSL context", e);
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
                    .childHandler(new HttpServerInitializer(sslCtx, router, sessionService));
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
