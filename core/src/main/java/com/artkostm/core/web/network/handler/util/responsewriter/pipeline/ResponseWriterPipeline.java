package com.artkostm.core.web.network.handler.util.responsewriter.pipeline;

import io.netty.handler.codec.http.HttpResponse;

import com.artkostm.core.web.network.handler.util.responsewriter.Stage;
import com.artkostm.core.web.network.handler.util.responsewriter.WriterPipeline;

public class ResponseWriterPipeline implements WriterPipeline<Object, HttpResponse>
{
    private Stage<Object, HttpResponse> first;
    private Stage<Object, HttpResponse> current;
    
    private boolean isFirst = true;
    
    @Override
    public WriterPipeline<Object, HttpResponse> add(Stage<Object, HttpResponse> stage)
    {
        if (isFirst)
        {
            first = stage;
            current = stage;
            isFirst = false;
        }
        else if (first != null && current != null)
        {
            current.successor(stage);
            current = stage;
        }
        
        return this;
    }

    @Override
    public HttpResponse run(Object... input)
    {
        return first.process(input);
    }
}
