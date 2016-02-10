package com.artkostm.core.controller;

import org.junit.Assert;
import org.junit.Test;

import com.artkostm.core.web.controller.Context;

public class TestContext
{
    private final Runnable client1 = new Runnable()
    {
        @Override
        public void run()
        {
            final Context context = new Context();
            context.addCookie("client1", "Artsiom");
            //Context.current.set(context);
            for (int i = 0; i < 10; i++)
            {
                Assert.assertNotNull(Context.current().getCookie("client1"));
                Assert.assertNull(Context.current().getCookie("client2"));
                Assert.assertTrue(Context.current().getCookie("client1").equals("Artsiom"));
                try
                {
                    Thread.sleep(100);
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
            }
        }
    };
    
    private final Runnable client2 = new Runnable()
    {
        @Override
        public void run()
        {
            final Context context = new Context();
            context.addCookie("client2", "Sasha");
            //Context.current.set(context);
            for (int i = 0; i < 10; i++)
            {
                Assert.assertNotNull(Context.current().getCookie("client2"));
                Assert.assertNull(Context.current().getCookie("client1"));
                Assert.assertTrue(Context.current().getCookie("client2").equals("Sasha"));
                try
                {
                    Thread.sleep(100);
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
            }
        }
    };
    
    @Test(expected = RuntimeException.class)
    public void testThreadedContext() throws InterruptedException
    {
        final Thread t1 = new Thread(client1);
        final Thread t2 = new Thread(client2);
        
        t1.start();
        t2.start();
        
        Thread.sleep(300);
        Assert.assertNull(Context.current());
    }
}
