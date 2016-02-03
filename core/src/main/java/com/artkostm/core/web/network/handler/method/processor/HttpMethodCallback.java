package com.artkostm.core.web.network.handler.method.processor;

import io.netty.buffer.ByteBuf;

import com.artkostm.core.web.controller.Context;

public interface HttpMethodCallback 
{
    void methodGet(final Context context);
    void methodPost(final Context context, final ByteBuf byteBuf);
}
