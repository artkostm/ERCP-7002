package com.artkostm.core.akka.typesafe;

import java.lang.reflect.Method;
import java.util.List;

import com.artkostm.core.configuration.BeanFactory;
import com.artkostm.core.configuration.ConfigAggregator;
import com.artkostm.core.configuration.internal.AppConfig;
import com.artkostm.core.network.handler.util.RequestMapper;
import com.artkostm.core.network.router.Router;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigResolveOptions;

public class Main
{
    public static void main(String[] args) throws Exception
    {
//        System.setProperty("name", "Artsiom");
//        
//        final Config resourceConfig = ConfigFactory.parseResources(Main.class, "/routees.config")
//                .resolve(ConfigResolveOptions.defaults().setUseSystemEnvironment(true));
//        
//
//        if (resourceConfig.hasPath("app.POST"))
//            System.out.println(resourceConfig.getAnyRefList("app.POST"));
//        
//        System.out.println(resourceConfig.getConfig("app").hasPath("GET"));
//        
//        System.out.println(BeanFactory.create(resourceConfig.getConfig("app"), AppConfig.class));
        Router<List<Method>> router = new Router<>();
        RequestMapper.map(router, ConfigAggregator.load("/routees.config").app());
        System.out.println(router.toString());
    }
}
