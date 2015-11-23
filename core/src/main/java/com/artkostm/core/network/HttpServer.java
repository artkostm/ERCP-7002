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

import java.net.SocketAddress;

public class HttpServer implements Runnable
{
    static final boolean SSL = System.getProperty("ssl") != null;
    
    private final SocketAddress address;
    
    public HttpServer(final SocketAddress addr)
    {
        address = addr;
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
                e.printStackTrace();
            }
            
        } else 
        {
            sslCtx = null;
        }
        
        final EventLoopGroup bossGroup = new NioEventLoopGroup();
        final EventLoopGroup workerGroup = new NioEventLoopGroup();
        try 
        {
            final ServerBootstrap b = new ServerBootstrap();
            b.option(ChannelOption.SO_BACKLOG, 1024);
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new HttpServerInitializer(sslCtx));
            final Channel ch = b.bind(address).sync().channel();
            ch.closeFuture().sync();
        }
        catch (InterruptedException e)
        {
            
        } 
        finally 
        {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
