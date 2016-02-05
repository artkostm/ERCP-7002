package com.artkostm.core.web.network.handler.util.responsewriter.pipeline;

import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;

import com.artkostm.core.web.controller.Result;
import com.artkostm.core.web.network.handler.util.responsewriter.Stage;

public class DefaultHeadersSetter extends Stage<Object, HttpResponse>
{
    @Override
    public HttpResponse process(final Object... input)
    {
        if (input == null || input.length != 3)
        {
            throw new RuntimeException("Expected input is [HttpResponse, HttpRequest, Result]");
        }
        final HttpResponse response = (HttpResponse) input[0];
        final HttpRequest request = (HttpRequest) input[1];
        final Result result = (Result) input[2];
        response.headers().set(HttpHeaders.Names.TRANSFER_ENCODING, HttpHeaders.Values.CHUNKED);
        response.headers().set(HttpHeaders.Names.CONTENT_TYPE, result != null ? result.getContentType() : "text/plain");
        // Disable cache by default
        response.headers().set(HttpHeaders.Names.CACHE_CONTROL, "no-cache, no-store, must-revalidate");
        response.headers().set(HttpHeaders.Names.PRAGMA, "no-cache");
        response.headers().set(HttpHeaders.Names.EXPIRES, "0");
        response.headers().set(HttpHeaders.Names.SERVER, "My Web Server/0.0.1");
        if (result.getContentLength() >= 0) 
        {
            response.headers().set(HttpHeaders.Names.CONTENT_LENGTH, result.getContentLength());
        }
        
        return successor.process(response, request, result);
    }
}
