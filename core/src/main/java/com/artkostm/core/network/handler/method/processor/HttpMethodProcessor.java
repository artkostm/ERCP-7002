package com.artkostm.core.network.handler.method.processor;

import io.netty.handler.codec.http.HttpObject;

import com.artkostm.core.controller.Context;

public interface HttpMethodProcessor<O>
{
    O process(final HttpObject request, final Context context);
}
