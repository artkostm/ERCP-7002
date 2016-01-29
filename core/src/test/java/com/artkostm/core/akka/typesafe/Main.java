package com.artkostm.core.akka.typesafe;

import com.artkostm.core.configuration.BeanFactory;
import com.artkostm.core.configuration.internal.AppConfig;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigResolveOptions;

public class Main
{
    public static void main(String[] args) throws Exception
    {
        System.setProperty("name", "Artsiom");
        
        final Config resourceConfig = ConfigFactory.parseResources(Main.class, "/routees.config")
                .resolve(ConfigResolveOptions.defaults().setUseSystemEnvironment(true));
        

        if (resourceConfig.hasPath("app.POST"))
            System.out.println(resourceConfig.getAnyRefList("app.POST"));
        
        System.out.println(resourceConfig.getConfig("app").hasPath("GET"));
        
        System.out.println(BeanFactory.create(resourceConfig.getConfig("app"), AppConfig.class));
    }
}
