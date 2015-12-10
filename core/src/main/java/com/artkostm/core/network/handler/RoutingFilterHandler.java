package com.artkostm.core.network.handler;

import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.util.List;

import com.artkostm.core.network.router.RouteResult;
import com.artkostm.core.network.router.Router;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.util.AttributeKey;

public class RoutingFilterHandler extends SimpleChannelInboundHandler<HttpObject>
{    
    private final Router<List<Method>> router;
    public static final AttributeKey<RouteResult<?>> routeResult = AttributeKey.valueOf("routeresult");
    private RouteResult<List<Method>> result;
    private boolean not_found;
    
    public RoutingFilterHandler(final Router<List<Method>> router)
    {
        this.router = router;
    }
    
    @Override
    public void channelReadComplete(final ChannelHandlerContext ctx) throws Exception 
    {
        ctx.channel().attr(routeResult).set(result);
        super.channelReadComplete(ctx);
    }
    
    @Override
    public boolean acceptInboundMessage(final Object msg) throws Exception 
    {
        // TODO may be there is an ability to set RouteResult object into HttpRequest object
        if (msg instanceof HttpRequest)
        {
            final HttpRequest request = (HttpRequest) msg;
            result = router.route(request.getMethod(), request.getUri());
            if (result == null || result.target() == null)
            {
                not_found = true;//not found
            }
        }
        return not_found;//message will be passed to the next Handler from the pipeline
    }

    @Override
    protected void channelRead0(final ChannelHandlerContext ctx, final HttpObject msg)
            throws Exception 
    {
        //Send "404 - Not found" if acceptInboundMessage(Object) returns true
        final DefaultFullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, 
                HttpResponseStatus.NOT_FOUND, Unpooled.copiedBuffer(NOT_FOUND_CONTENT, Charset.forName("UTF-8")));
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }
    
    private static final String NOT_FOUND_CONTENT = "<h1>404 - Not found</h1>";
}
