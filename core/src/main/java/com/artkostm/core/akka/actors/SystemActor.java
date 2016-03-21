package com.artkostm.core.akka.actors;

import java.nio.charset.Charset;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;

import com.artkostm.core.akka.http.message.HttpMessage;

import akka.actor.UntypedActor;

public abstract class SystemActor extends UntypedActor
{
    @Override
    public void onReceive(Object msg) throws Exception
    {
        if (msg instanceof HttpMessage)
        {
            onRequest((HttpMessage) msg);
        }
        else 
        {
            unhandled(msg);
        }
    }
    
    protected abstract void onRequest(HttpMessage msg) throws Exception;
    
    protected void notFound(HttpMessage msg)
    {
        final ChannelHandlerContext context = msg.context();
        final ByteBuf buf = Unpooled.copiedBuffer(NOT_FOUND_CONTENT, Charset.forName("UTF-8"));
        final DefaultFullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, 
            HttpResponseStatus.NOT_FOUND, buf);
        context.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }
    
    private static final String NOT_FOUND_CONTENT = "<h1>404 - Not found</h1>";
}
