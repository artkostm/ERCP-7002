package com.artkostm.core.akka.http.client.common;

import java.util.concurrent.CompletionStage;
import java.util.function.BiConsumer;

import akka.http.javadsl.model.HttpRequest;
import akka.http.javadsl.model.HttpResponse;

public interface SingleRequestRunner
{
    ResponseHandler request(String url);
    ResponseHandler request(HttpRequest request);
    CompletionStage<HttpResponse> request(String url, BiConsumer<? super HttpResponse, ? super Throwable> action);
    CompletionStage<HttpResponse> request(HttpRequest request, BiConsumer<? super HttpResponse, ? super Throwable> action);
}