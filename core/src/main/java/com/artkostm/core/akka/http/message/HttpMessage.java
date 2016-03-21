package com.artkostm.core.akka.http.message;

import io.netty.handler.codec.http.HttpRequest;

import com.artkostm.core.akka.http.HttpMethods;
import com.artkostm.core.netty.ChannelHandlerContexAware;

public interface HttpMessage extends ChannelHandlerContexAware
{
    HttpMethods method();
    HttpRequest request();
}
