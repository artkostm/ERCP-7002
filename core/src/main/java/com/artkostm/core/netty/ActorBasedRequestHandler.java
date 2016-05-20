package com.artkostm.core.netty;

import java.nio.charset.Charset;

import com.artkostm.core.akka.http.message.AppInternalMessage;

import akka.actor.ActorRef;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpMethod;

public class ActorBasedRequestHandler extends SimpleChannelInboundHandler<FullHttpRequest>
{
    private final ActorRef router;
    
    public ActorBasedRequestHandler(final ActorRef router)
    {
        this.router = router;
    }
    
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception 
    {
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest msg) throws Exception
    {
        String content = null;
        if(msg.getMethod().equals(HttpMethod.POST) || msg.getMethod().equals(HttpMethod.PUT)) 
        {
            content = msg.content().toString(Charset.forName("UTF-8"));
        }
        AppInternalMessage message = new AppInternalMessage(msg, ctx);
        message.payload(content);
        router.tell(message, ActorRef.noSender());
    }
}