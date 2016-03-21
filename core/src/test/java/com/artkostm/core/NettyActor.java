package com.artkostm.core;

import org.apache.commons.io.IOUtils;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.handler.codec.http.DefaultHttpResponse;
import io.netty.handler.codec.http.DefaultLastHttpContent;
import io.netty.handler.codec.http.HttpChunkedInput;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.LastHttpContent;
import io.netty.handler.stream.ChunkedStream;
import akka.actor.UntypedActor;

import com.artkostm.core.akka.http.message.HttpMessage;

public class NettyActor extends UntypedActor
{
    @Override
    public void onReceive(Object msg) throws Exception 
    {
        if (msg instanceof HttpMessage)
        {
            final HttpResponse response = new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, true);
            ((HttpMessage) msg).context().write(response);
            ((HttpMessage) msg).context().write(new HttpChunkedInput(new ChunkedStream(IOUtils.toInputStream("{\"message\":\"Hello, World!\"}"))));
            LastHttpContent fs = new DefaultLastHttpContent();
            ChannelFuture sendContentFuture = ((HttpMessage) msg).context().writeAndFlush(fs);
            sendContentFuture.addListener(ChannelFutureListener.CLOSE);
        }
    }
}