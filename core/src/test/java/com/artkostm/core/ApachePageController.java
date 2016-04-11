package com.artkostm.core;

import com.artkostm.core.akka.actors.CamelControllerActor;
import com.artkostm.core.akka.http.message.HttpMessage;
import com.artkostm.core.web.controller.Result;

public class ApachePageController extends CamelControllerActor
{
    @Override
    protected Result onRequest(HttpMessage msg) throws Exception
    {
        return ok((String) msg.payload());
    }

    @Override
    protected String getProducerUri()
    {
        return "http://localhost:8060/hello";
    }

    @Override
    protected String getConsumerUri()
    {
        return "direct:start";
    }
}
