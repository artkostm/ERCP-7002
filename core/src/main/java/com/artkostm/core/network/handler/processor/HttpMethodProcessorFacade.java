package com.artkostm.core.network.handler.processor;

import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpRequest;

import java.util.HashMap;
import java.util.Map;

public class HttpMethodProcessorFacade implements HttpMethodProcessor
{
    private static final Map<HttpMethod, HttpMethodProcessor> processors;
    
    static
    {
        processors = new HashMap<HttpMethod, HttpMethodProcessor>();
        processors.put(HttpMethod.GET, new GETMethodProcessor());
        processors.put(HttpMethod.POST, new POSTMethodProcessor());
        processors.put(HttpMethod.PUT, new PUTMethodProcessor());
        processors.put(HttpMethod.DELETE, new DELETEMethodProcessor());
    }

    @Override
    public void process(Object request)
    {
        final HttpRequest httpRequest = (HttpRequest) request;
        final HttpMethodProcessor processor = processors.get(httpRequest.getMethod());
        if (processor != null)
        {
            processor.process(httpRequest);
        }
    }
}
