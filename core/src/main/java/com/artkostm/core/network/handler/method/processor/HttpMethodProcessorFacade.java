package com.artkostm.core.network.handler.method.processor;

import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.LastHttpContent;

import java.util.HashMap;
import java.util.Map;

import com.artkostm.core.controller.Context;
import com.artkostm.core.network.handler.content.BodyConsumer;

public class HttpMethodProcessorFacade implements HttpMethodProcessor<Object>
{
    private static final Map<HttpMethod, HttpMethodProcessor> processors;
    
    static
    {
        processors = new HashMap<HttpMethod, HttpMethodProcessor>();
        processors.put(HttpMethod.GET, new NoBodyRepresentedMethodProcessor());
        processors.put(HttpMethod.POST, new BodyRepresentedMethodProcessor());
    }
    
    private final HttpMethodProcessor requestProcessor;
    private final HttpMethodProcessor contentProcessor;
    
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
