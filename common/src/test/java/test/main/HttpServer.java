package test.main;

import by.artkostm.di.annotation.Inject;
import by.artkostm.di.annotation.Singleton;
import test.main.inner.HttpServerInitializer;

@Singleton
public class HttpServer
{
    @Inject
    private HttpServerInitializer initializer;
    
    public void get()
    {
        System.out.println("TEST:"+initializer.getClass().getName());
    }
}
