package com.artkostm.core.guice;

import java.lang.reflect.Method;
import java.util.ArrayList;

import io.netty.handler.codec.http.HttpMethod;

import com.artkostm.core.configuration.Configuration;
import com.google.inject.Guice;
import com.google.inject.Injector;

public class Main
{
    public static void main(String[] args)
    {
        final Injector injector = Guice.createInjector(new Configuration("classpath:luceneapp.gr"));
        final TestCl cl = injector.getInstance(TestCl.class);
        System.out.println(cl);
        System.out.println("Metadata: " + cl.Metadata());
        cl.router().addRoute(HttpMethod.GET, "url1", new ArrayList<Method>());
        System.out.println(cl);
        cl.router().addRoute(HttpMethod.PUT, "url2", new ArrayList<Method>());
        final TestCl c2 = injector.getInstance(TestCl.class);
        System.out.println(c2);
        System.out.println("Metadata: " + c2.Metadata());
        
    }
}
