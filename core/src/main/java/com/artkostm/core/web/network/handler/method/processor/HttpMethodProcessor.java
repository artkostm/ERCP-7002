package com.artkostm.core.web.network.handler.method.processor;

import io.netty.handler.codec.http.HttpObject;

import com.artkostm.core.web.controller.Context;

/**
 * 
 * @author Artsiom_Chuiko 
 *
 * @param <O>
 */
public interface HttpMethodProcessor<O>
{
    O process(final HttpObject request, final Context context);
}
