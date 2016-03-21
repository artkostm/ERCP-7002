package com.artkostm.core.akka.http.client.common;

public interface HttpFlowBuilder
{
    HttpRequestRunner createFlow(final String host, final int port);
}
