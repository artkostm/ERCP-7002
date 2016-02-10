package com.artkostm.core.web.network.handler.method.processor;

import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.HttpRequest;

import com.artkostm.core.web.controller.Context;

/**
 * 
 * @author Artsiom_Chuiko
 *
 */
public class HttpMethodProcessorFacade implements HttpMethodProcessor<Object>
{    
    private final HttpMethodProcessor<?> requestProcessor;
    private final HttpMethodProcessor<?> contentProcessor;
    
    public HttpMethodProcessorFacade()
    {
        requestProcessor = new HttpRequestProcessor();
        contentProcessor = new HttpContentProcessor();
    }

    @Override
    public Object process(final HttpObject request, final Context context)
    {
        if (request instanceof HttpRequest)
        {
            return requestProcessor.process(request, context);
        }
        if (request instanceof HttpContent)
        {
            return contentProcessor.process(request, context);
        }
        return null;
    }
}
