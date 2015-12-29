package com.artkostm.configurator;

import org.junit.Test;

public class ConfiguratorTest
{
    @Test
    public void testWithNullConfigPath()
    {
        final Configurator configurator = new Configurator();
        configurator.createConfiguration(null);
    }
}
