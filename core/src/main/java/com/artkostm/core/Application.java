package com.artkostm.core;

import java.lang.reflect.Method;
import java.util.List;

import com.artkostm.configurator.Configurator;
import com.artkostm.configurator.model.Metadata;
import com.artkostm.core.network.router.Router;

public interface Application extends Runnable, ApplicationConstants
{
    Metadata configure();
    Router<List<Method>> router();
    Configurator configurator(); 
}
