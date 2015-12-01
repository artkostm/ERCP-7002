package com.artkostm.core.network.handler.method.processor;

import com.artkostm.core.controller.Context;

public interface HttpMethodProcessor
{
    Context process(Object request);
}
