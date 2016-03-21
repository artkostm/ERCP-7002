package com.artkostm.core.netty;

import com.artkostm.core.akka.http.message.AppInternalMessage;

import akka.actor.ActorRef;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.HttpRequest;

public class ActorBasedRequestHandler extends ChannelInboundHandlerAdapter
{
    private final ActorRef router;
    
    public ActorBasedRequestHandler(final ActorRef router)
    {
        this.router = router;
    }
    
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception
    {
        if (msg instanceof HttpRequest)
        {
            router.tell(new AppInternalMessage((HttpRequest) msg, ctx), ActorRef.noSender());
        }
    }
    
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception 
    {
        cause.printStackTrace();
        ctx.close();
    }
}