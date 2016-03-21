package com.artkostm.core.akka.configuration;

import com.artkostm.core.web.network.router.Router;
import com.typesafe.config.Config;

public interface RouterProvider<T>
{
    Router<T> get(Config config);
}
