package com.artkostm.core.akka;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import scala.concurrent.Await;
import scala.concurrent.duration.Duration;
import scala.concurrent.duration.FiniteDuration;

import com.artkostm.core.akka.aktors.BinaryEchoAktor;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigResolveOptions;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.japi.Util;
import akka.pattern.Patterns;
import akka.util.Timeout;

public class Main 
{
    //5095931000566324969
    public static void main(String[] args) throws Exception
    {
        System.setProperty("name", "Artsiom");
        final ActorSystem system = ActorSystem.create("testsystem");
//        final ActorRef kernel = system.actorOf(Props.create(Kernel.class), "kernel");
//        final long start = System.currentTimeMillis();
//        final FiniteDuration duration = Duration.create(50, TimeUnit.MINUTES);
//        Await.result(Patterns.ask(kernel, "5095931000566324969", new Timeout(duration)), Duration.Inf());
//        System.out.println("Time is " + (System.currentTimeMillis() - start) + " ms");
        
        final ActorRef echo = system.actorOf(Props.create(BinaryEchoAktor.class), "echo");
        final FiniteDuration duration = Duration.create(1, TimeUnit.SECONDS);
        final String binaryResult = Await.result(Patterns.ask(echo, 1032L, new Timeout(duration)).mapTo(Util.classTag(String.class)), Duration.Inf());
        System.out.println(binaryResult);
        
        Await.ready(system.terminate(), Duration.Inf());
        
        final Config config = ConfigFactory.parseString("my { config{val1=[{}, {}, {}]\nval2=value2}}");
        final List<? extends Object> list = config.getAnyRefList("my.config.val1");
        
        final Config resourceConfig = ConfigFactory.parseResources(Main.class, "/app.cfg").resolve(ConfigResolveOptions.defaults().setUseSystemEnvironment(true));
        final List<? extends Object> resourceList = resourceConfig.getAnyRefList("app.config.val1");
        
        System.out.println(list.get(0).getClass().getName());
        System.out.println(resourceList);
        System.out.println(resourceConfig.getString("app.config.val3"));
        
        final Base base = (Base) Proxy.newProxyInstance(Base.class.getClassLoader(), new Class[]{Base.class}, new Handler());
        
        base.run();
    }
    
    public static long counter()
    {
        return counter++;
    }
    
    private static long counter = 2;
    
    public static class Handler implements InvocationHandler
    {
        private final Base hiden = new Base()
        {
            @Override
            public void run()
            {
                System.out.println("RUN");
            }
        };
        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable
        {
            System.out.println("Before");
            final Object obj = method.invoke(hiden, args);
            System.out.println("After");
            return obj;
        }
    }
    
    public static interface Base
    {
        void run();
    }
}