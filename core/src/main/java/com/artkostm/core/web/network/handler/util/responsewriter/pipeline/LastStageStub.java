package com.artkostm.core.web.network.handler.util.responsewriter.pipeline;

import io.netty.handler.codec.http.HttpResponse;

import com.artkostm.core.web.network.handler.util.responsewriter.Stage;

public class LastStageStub extends Stage<Object, HttpResponse>
{
    @Override
    public HttpResponse process(Object... input)
    {
        if (input == null || input.length == 0)
        {
            throw new RuntimeException("Expected input is [HttpResponse, ...]");
        }
        final HttpResponse response = (HttpResponse) input[0];
        return response;
    }
}
