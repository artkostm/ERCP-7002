package com.artkostm.core.akka.http.client.common;

import scala.concurrent.Future;
import akka.dispatch.OnComplete;
import akka.http.javadsl.model.HttpRequest;
import akka.http.javadsl.model.HttpResponse;

public interface SingleRequestRunner
{
    ResponseHandler create(String url);
    ResponseHandler create(HttpRequest request);
    Future<HttpResponse> create(String url, OnComplete<HttpResponse> onComplete);
    Future<HttpResponse> create(HttpRequest request, OnComplete<HttpResponse> onComplete);
}