package com.artkostm.core.network.handler;

import com.artkostm.core.network.handler.method.processor.HttpMethodProcessor;
import com.artkostm.core.network.handler.method.processor.HttpMethodProcessorFacade;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.HttpRequest;

public class SecondHttpServerHandler extends SimpleChannelInboundHandler<HttpObject>
{
    private final HttpMethodProcessor methodFacade;
    
    public SecondHttpServerHandler()
    {
        methodFacade = new HttpMethodProcessorFacade();
    }
    
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg) throws Exception
    {
        if (msg instanceof HttpRequest)
        {
            methodFacade.process(msg);
        }
    }
    
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception
    {
        System.out.println("channelReadComplete2");
        super.channelReadComplete(ctx);
    }
}
