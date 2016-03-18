package com.artkostm.core.web.network.handler;

import java.io.Serializable;

import org.apache.commons.io.IOUtils;

import com.artkostm.core.akka.http.routing.HttpMethodRoutingPool;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.UntypedActor;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultHttpResponse;
import io.netty.handler.codec.http.DefaultLastHttpContent;
import io.netty.handler.codec.http.HttpChunkedInput;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.LastHttpContent;
import io.netty.handler.stream.ChunkedStream;

public class HttpAkkaHandler extends SimpleChannelInboundHandler<HttpObject>
{
    private static final ActorSystem system = ActorSystem.create("netty");
    
    static final ActorRef netty = system.actorOf(Props.create(NettyActor.class).withRouter(new HttpMethodRoutingPool(30)));
    
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg) throws Exception 
    {
        netty.tell(new Msg(ctx), ActorRef.noSender());
    }
    
    public static class NettyActor extends UntypedActor
    {

        @Override
        public void onReceive(Object msg) throws Exception {
            if (msg instanceof Msg)
            {
                final HttpResponse response = new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, true);
                ((Msg) msg).getCtx().write(response);
                ((Msg) msg).getCtx().write("{\"message\":\"Hello, World!\"}");
                LastHttpContent fs = new DefaultLastHttpContent();
                ChannelFuture sendContentFuture = ((Msg) msg).getCtx().writeAndFlush(fs);
                sendContentFuture.addListener(ChannelFutureListener.CLOSE);
            }
        }
        
    }
    
    public static class Msg implements Serializable
    {
        private static final long serialVersionUID = 5302850716429410923L;
        
        private final ChannelHandlerContext ctx;
        
        public Msg(final ChannelHandlerContext ctx) 
        {
            this.ctx = ctx;
        }

        public ChannelHandlerContext getCtx() {
            return ctx;
        }
    }
}
