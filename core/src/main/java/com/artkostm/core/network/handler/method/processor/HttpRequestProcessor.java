package com.artkostm.core.network.handler.method.processor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.QueryStringDecoder;
import io.netty.handler.codec.http.cookie.Cookie;
import io.netty.handler.codec.http.cookie.ServerCookieDecoder;

import com.artkostm.core.controller.Context;

public class HttpRequestProcessor implements HttpMethodProcessor<Context>
{
    @Override
    public Context process(final HttpObject request, final Context context)
    {
        if (request instanceof HttpRequest)
        {
            final HttpRequest httpRequest = (HttpRequest) request;
            final QueryStringDecoder queryStringDecoder = new QueryStringDecoder(httpRequest.getUri());
            context.getQueryParams().putAll(queryStringDecoder.parameters());
            context.getCookies().putAll(readCookie(httpRequest));
        }
        
        return context;
    }
    
    private Map<String, List<Cookie>> readCookie(final HttpRequest httpRequest)
    {
        final Map<String, List<Cookie>> cookiesDownload = new HashMap<>();
        final String cookieString = httpRequest.headers().get(HttpHeaders.Names.COOKIE);
        if (cookieString != null) 
        {
            for (Cookie cookie : ServerCookieDecoder.STRICT.decode(cookieString))
            {
                if (cookiesDownload.containsKey(cookie.name())) 
                {
                    cookiesDownload.get(cookie.name()).add(cookie);
                } 
                else 
                {
                    cookiesDownload.put(cookie.name(), new ArrayList<>(Arrays.asList(cookie)));
                }
            }
        }
        return cookiesDownload;
    }
}
