package com.artkostm.core.akka.extension.router;

import java.lang.reflect.Method;
import java.util.List;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

/**
 * 
 * @author Artsiom_Chuiko
 *
 */
@Singleton
public class MethodRouterProvider implements Provider<Router<List<Method>>>
{
    private final Router<List<Method>> delegate;
    
    @Inject
    public MethodRouterProvider()
    {
        delegate = new Router<List<Method>>();
        delegate.notFound(null);
    }

    @Override
    public Router<List<Method>> get()
    {
        return delegate; //TODO: new Router(delegate);
    }
}
