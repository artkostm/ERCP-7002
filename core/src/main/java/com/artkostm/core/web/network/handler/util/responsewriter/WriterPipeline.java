package com.artkostm.core.web.network.handler.util.responsewriter;

public interface WriterPipeline<I, O>
{
    WriterPipeline<I, O> add(Stage<I, O> stage);
    
    @SuppressWarnings("unchecked")
    O run(I...input);
}
