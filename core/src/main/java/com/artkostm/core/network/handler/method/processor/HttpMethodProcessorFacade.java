package com.artkostm.core.network.handler.method.processor;

import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpRequest;

import java.util.HashMap;
import java.util.Map;

import com.artkostm.core.controller.Context;

public class HttpMethodProcessorFacade implements HttpMethodProcessor
{
    private static final Map<HttpMethod, HttpMethodProcessor> processors;
    
    static
    {
        processors = new HashMap<HttpMethod, HttpMethodProcessor>();
        processors.put(HttpMethod.GET, new NoBodyRepresentedMethodProcessor());
        processors.put(HttpMethod.POST, new BodyRepresentedMethodProcessor());
    }

    @Override
    public Context process(Object request)
    {
        if (request instanceof HttpRequest)
        {
            final HttpRequest httpRequest = (HttpRequest) request;
            final HttpMethodProcessor processor = processors.get(httpRequest.getMethod());
            if (processor != null)
            {
                processor.process(httpRequest);
            }
        }
        return null;
    }
}
