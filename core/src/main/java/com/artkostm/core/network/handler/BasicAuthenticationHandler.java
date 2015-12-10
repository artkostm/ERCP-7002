package com.artkostm.core.network.handler;

import java.nio.charset.Charset;

import com.artkostm.core.network.handler.auth.Authenticator;
import com.artkostm.core.network.handler.auth.Credentials;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;

public class BasicAuthenticationHandler extends SimpleChannelInboundHandler<HttpObject> implements Authenticator
{
    private final Authenticator authenticator;
    private boolean authenticated;
    
    public BasicAuthenticationHandler(final Authenticator authenticator)
    {
        this.authenticator = authenticator;
    }
    
    @Override
    public boolean acceptInboundMessage(Object msg) throws Exception
    {
        if (msg instanceof HttpRequest)
        {
            final HttpRequest request = (HttpRequest) msg;
            final String auth = request.headers().get(HttpHeaders.Names.AUTHORIZATION);
            final Credentials credentials = new Credentials(auth);
            if (authenticator == null)
            {
                authenticated = authenticate(credentials);
            }
            else
            {
                authenticated = authenticator.authenticate(credentials);
            }
        }
        return !authenticated;
    }
    
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg) throws Exception
    {
        final DefaultFullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, 
            HttpResponseStatus.UNAUTHORIZED, Unpooled.copiedBuffer("<h1>401 - Unauthofized</h1>", Charset.forName("UTF-8")));
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }

    @Override
    public boolean authenticate(final Credentials cr)
    {
        if (cr == null || cr.getAuth() == null || cr.getAuth().isEmpty())
        {
            return false;
        }
        
        return true;
    }
}
