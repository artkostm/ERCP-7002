package com.artkostm.core.network;

import com.artkostm.core.network.handler.HttpServerHandler;
import com.artkostm.core.network.handler.RoutingFilterHandler;
import com.artkostm.core.network.router.MethodRouterProvider;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.stream.ChunkedWriteHandler;

@Singleton
public class HttpServerInitializer extends ChannelInitializer<SocketChannel>
{
    private SslContext sslCtx;
    
    @Inject
    private MethodRouterProvider routerProvider;
    
    @Override
    protected void initChannel(final SocketChannel ch) throws Exception
    {
        final ChannelPipeline p = ch.pipeline();
        if (sslCtx != null) 
        {
            p.addLast(sslCtx.newHandler(ch.alloc()));
        }

        p.addLast(new HttpRequestDecoder());
        p.addLast(new HttpResponseEncoder());
        p.addLast(new ChunkedWriteHandler());
        //p.addLast("authentication", null);
        p.addLast("routingfilter", new RoutingFilterHandler(routerProvider.get()));
        p.addLast("basic", new HttpServerHandler(routerProvider.get()));
        //p.addLast(new SecondHttpServerHandler());
    }
    
    public void setSslContext(final SslContext sslCtx)
    {
        this.sslCtx = sslCtx;
    }
}
