package com.artkostm.core.akka.typesafe;

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
        
        System.out.println(resourceConfig.getAnyRefList("app.GET"));
        if (resourceConfig.hasPath("app.POST"))
            System.out.println(resourceConfig.getAnyRefList("app.POST"));
    }
}
