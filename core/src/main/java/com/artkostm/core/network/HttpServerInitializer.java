package com.artkostm.core.network;

import com.artkostm.core.network.handler.HttpServerHandler;

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

    public HttpServerInitializer(SslContext sslCtx) 
    {
        this.sslCtx = sslCtx;
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
        
        p.addLast(new HttpServerHandler());
        //p.addLast(new SecondHttpServerHandler());
    }
}
