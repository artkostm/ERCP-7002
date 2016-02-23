package com.artkostm.core.web.network;

import com.artkostm.core.web.network.handler.HttpAkkaHandler;
import com.artkostm.core.web.network.handler.HttpServerHandler;
import com.artkostm.core.web.network.handler.RoutingFilterHandler;
import com.artkostm.core.web.network.router.MethodRouterProvider;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.stream.ChunkedWriteHandler;

/**
 * 
 * @author Artsiom_Chuiko
 *
 */
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

//        p.addLast(new HttpRequestDecoder());
//        p.addLast(new HttpResponseEncoder());
//        p.addLast(new ChunkedWriteHandler());
//        //p.addLast("authentication", null);
//        p.addLast("routingfilter", new RoutingFilterHandler(routerProvider.get()));
//        p.addLast("basic", new HttpServerHandler(routerProvider.get()));
        //p.addLast(new SecondHttpServerHandler());
        
        p.addLast("decoder"       , new HttpRequestDecoder());
        p.addLast("aggregator"    , new HttpObjectAggregator(Integer.MAX_VALUE));
        p.addLast("encoder"       , new HttpResponseEncoder());
        p.addLast("chunkedWriter" , new ChunkedWriteHandler());
        p.addLast("akka" , new HttpAkkaHandler());
    }
    
    public void setSslContext(final SslContext sslCtx)
    {
        this.sslCtx = sslCtx;
    }
}
