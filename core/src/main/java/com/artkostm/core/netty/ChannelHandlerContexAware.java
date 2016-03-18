package com.artkostm.core.netty;

import io.netty.channel.ChannelHandlerContext;

public interface ChannelHandlerContexAware
{
    ChannelHandlerContext context();
    void context(final ChannelHandlerContext context);
}
