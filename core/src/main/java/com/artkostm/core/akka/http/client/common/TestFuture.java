package com.artkostm.core.akka.http.client.common;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeoutException;

import scala.concurrent.Awaitable;
import scala.concurrent.CanAwait;
import scala.concurrent.duration.Duration;

public class TestFuture<T> extends CompletableFuture<T> implements Awaitable<T>
{

    @Override
    public Awaitable<T> ready(Duration duration, CanAwait can) throws TimeoutException, InterruptedException
    {
        
        return null;
    }

    @Override
    public T result(Duration duration, CanAwait can) throws Exception
    {
        return null;
    }

}
