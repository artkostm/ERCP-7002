package com.artkostm.core.web.network.handler.util.responsewriter;

public abstract class Stage<I, O>
{
    protected Stage<I, O> successor;
    
    public void successor(Stage<I, O> successor)
    {
        this.successor = successor;
    }
    
    @SuppressWarnings("unchecked")
    public abstract O process(I...input);
}
