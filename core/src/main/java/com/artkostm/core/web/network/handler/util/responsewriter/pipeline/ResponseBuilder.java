package com.artkostm.core.web.network.handler.util.responsewriter.pipeline;

import java.lang.reflect.Method;
import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http.DefaultHttpResponse;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;

import com.artkostm.core.web.controller.Context;
import com.artkostm.core.web.controller.ControllerMethodInvoker;
import com.artkostm.core.web.controller.Result;
import com.artkostm.core.web.network.handler.util.HttpContentReader;
import com.artkostm.core.web.network.handler.util.responsewriter.Stage;
import com.artkostm.core.web.network.router.RouteResult;

public class ResponseBuilder extends Stage<Object, HttpResponse>
{
    @SuppressWarnings("unchecked")
    @Override
    public HttpResponse process(Object... input)
    {
        if (input == null || input.length != 3)
        {
            throw new RuntimeException("Expected input is [RouteResult<List<Method>>, ByteBuf, HttpRequest]");
        }
        
        final RouteResult<List<Method>> routeResult = (RouteResult<List<Method>>) input[0];
        final ByteBuf contentBuffer = (ByteBuf) input[1];
        final HttpRequest request = (HttpRequest) input[2];
        Context.current().setPathParams(routeResult.pathParams());
        //TODO: set content
        Context.current().setContent(HttpContentReader.read(contentBuffer));
        final Result result = ControllerMethodInvoker.invoke(routeResult.target());
        final HttpResponse response = new DefaultHttpResponse(HttpVersion.HTTP_1_1, 
                result == null ? HttpResponseStatus.NO_CONTENT : HttpResponseStatus.OK, true);
        return successor.process(response, request, result);
    }
}
