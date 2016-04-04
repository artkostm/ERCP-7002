package com.artkostm.core;

import com.artkostm.core.akka.actors.ControllerActor;
import com.artkostm.core.akka.http.message.HttpMessage;
import com.artkostm.core.controller.ControllerForTest.Message;
import com.artkostm.core.web.controller.Result;
import com.artkostm.core.web.controller.converter.Json;

public class NettyActor extends ControllerActor
{
    @Override
    protected Result onRequest(HttpMessage msg) throws Exception
    {
        return ok(Json.toJson(new Message("Hello, World!")).toString()).asJson();
    }
}