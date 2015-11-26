package com.artkostm.core.network.handler;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.LastHttpContent;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;

/**
 * This utility handler should be put at the last position of the inbound pipeline to catch all exceptions caused by bad
 * client (closed connection, malformed request etc.) and server processing, then close the connection.
 *
 * By default exceptions are logged to Netty internal logger. You may need to override {@link #onUnknownMessage(Object)}
 * , {@link #onBadClient(Throwable)}, and {@link #onBadServer(Throwable)} to log to more suitable places.
 */
@Sharable
public class BadClientSilencer extends SimpleChannelInboundHandler<Object>
{
    private static final InternalLogger log = InternalLoggerFactory.getInstance(BadClientSilencer.class);

    protected void onUnknownMessage(Object msg)
    {
        log.warn("Unknown msg: " + msg);
    }

    protected void onBadClient(Throwable e)
    {
        log.warn("Caught exception (maybe client is bad)", e);
    }

    protected void onBadServer(Throwable e)
    {
        log.warn("Caught exception (maybe server is bad)", e);
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, Object msg)
    {
        // This handler is the last inbound handler.
        // This means msg has not been handled by any previous handler.
        ctx.close();

        if (msg != LastHttpContent.EMPTY_LAST_CONTENT)
        {
            onUnknownMessage(msg);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable e)
    {
        ctx.close();

        // To clarify where exceptions are from, imports are not used
        if (e instanceof java.io.IOException || // Connection reset by peer, Broken pipe
        e instanceof java.nio.channels.ClosedChannelException ||
            e instanceof io.netty.handler.codec.DecoderException ||
            e instanceof io.netty.handler.codec.CorruptedFrameException || // Bad WebSocket frame
            e instanceof java.lang.IllegalArgumentException || // Use https://... to connect to HTTP server
            e instanceof javax.net.ssl.SSLException || // Use http://... to connect to HTTPS server
            e instanceof io.netty.handler.ssl.NotSslRecordException)
        {
            onBadClient(e); // Maybe client is bad
        }
        else
        {
            onBadServer(e); // Maybe server is bad
        }
    }
}
