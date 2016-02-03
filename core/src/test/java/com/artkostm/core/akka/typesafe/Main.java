package com.artkostm.core.akka.typesafe;

import java.lang.reflect.Method;
import java.util.List;

import com.artkostm.core.configuration.ConfigAggregator;
import com.artkostm.core.web.network.handler.util.RequestMapper;
import com.artkostm.core.web.network.router.Router;

public class Main
{
    public static void main(String[] args) throws Exception
    {
        Router<List<Method>> router = new Router<>();
        RequestMapper.map(router, ConfigAggregator.load("/routees.config").app());
        System.out.println(router.toString());
    }
}
