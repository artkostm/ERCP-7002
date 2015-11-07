package test.main;

import javax.annotation.PreDestroy;

import by.artkostm.di.annotation.Inject;

public class Stub2
{
    @Inject(name = "st1")
    private Stub1 st1;
    
    private String m;
    
    public Stub2()
    {}
    
    public Stub2(String str)
    {
        m = str;
    }

    public static void m1()
    {}

    @PreDestroy
    public void m2()
    {
        System.out.println("DESTROY");
    }

    @SuppressWarnings("unused")
    private void m3()
    {}

    @Override
    public String toString()
    {
        return "Stub2 [st1=" + st1 + ", m=" + m + "]";
    }
}
