package com.artkostm.core.web.network.handler.content;

import io.netty.handler.codec.http.HttpContent;

/**
 * 
 * @author Artsiom_Chuiko
 *
 */
public interface BodyConsumer 
{
    void chunk(final HttpContent content);
    void finished();
    void handleError(final Throwable t);
}
