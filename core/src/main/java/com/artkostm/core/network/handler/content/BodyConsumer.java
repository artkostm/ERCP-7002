package com.artkostm.core.network.handler.content;

import io.netty.handler.codec.http.HttpContent;

public interface BodyConsumer 
{
    void chunk(final HttpContent content);
    void finished();
    void handleError(final Throwable t);
}
