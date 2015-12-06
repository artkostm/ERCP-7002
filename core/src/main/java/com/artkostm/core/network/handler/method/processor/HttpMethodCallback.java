package com.artkostm.core.network.handler.method.processor;

import io.netty.buffer.ByteBuf;

import com.artkostm.core.controller.Context;

public interface HttpMethodCallback 
{
    void methodGet(final Context context);
    void methodPost(final Context context, final ByteBuf byteBuf);
}
