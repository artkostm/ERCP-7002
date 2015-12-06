package com.artkostm.core.network;

import java.lang.reflect.Method;
import java.util.List;

import com.artkostm.core.controller.session.SessionService;
import com.artkostm.core.network.handler.HttpServerHandler;
import com.artkostm.core.network.handler.RoutingFilterHandler;
import com.artkostm.core.network.router.Router;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.stream.ChunkedWriteHandler;

public class HttpServerInitializer extends ChannelInitializer<SocketChannel>
{
    private final SslContext sslCtx;
    private final Router<List<Method>> router;
    private final SessionService sessionService;

    public HttpServerInitializer(final SslContext sslCtx, final Router<List<Method>> router, final SessionService sessionService) 
    {
        this.sslCtx = sslCtx;
        this.router = router;
        this.sessionService = sessionService;
    }
    
    @Override
    protected void initChannel(SocketChannel ch) throws Exception
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
        p.addLast("routingfilter", new RoutingFilterHandler(router));
        p.addLast("basic", new HttpServerHandler(router, sessionService));
        //p.addLast(new SecondHttpServerHandler());
    }
}
