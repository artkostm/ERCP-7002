package com.artkostm.core.network.handler.content;

import io.netty.handler.codec.http.HttpContent;

public interface BodyConsumer 
{
    void chunk(final HttpContent content);
    Object finished();
    void handleError(final Throwable t);
}
