package com.artkostm.configurator;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.artkostm.configurator.model.Metadata;
import com.artkostm.configurator.model.RouteConfig;

public class BaseTest 
{
    @Test
    public void testConfigurator()
    {
        final Configurator configurator = new Configurator();
        final Metadata configuration = configurator.createConfiguration("classpath:testconfiguration.gr");
        assertNotNull(configurator.getDirectoryForTemplateLoading());
        assertTrue(configurator.getPort() == 8080);
        assertNotNull(configuration);
        assertNotNull(configuration.getRouteConfigList());
        assertTrue(!configuration.getRouteConfigList().isEmpty());
        final RouteConfig routeConfig = configuration.getRouteConfigList().get(0);
        assertNotNull(routeConfig);
        assertNotNull(routeConfig.controllers());
        assertTrue(!routeConfig.controllers().isEmpty());
        assertTrue("post".equalsIgnoreCase(routeConfig.method()));
    }
}
