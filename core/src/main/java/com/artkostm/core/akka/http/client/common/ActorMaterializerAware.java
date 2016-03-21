package com.artkostm.core.akka.http.client.common;

import akka.stream.ActorMaterializer;

public interface ActorMaterializerAware
{
    ActorMaterializer materializer();
}
