package test.main;

import javax.annotation.PostConstruct;

import by.artkostm.di.annotation.Inject;

public class Stub1
{
    private String f1;
    @Inject
    private Object getInt;
    
    @PostConstruct
    public void init()
    {
        f1 = "Hello world";
    }

    public String getF1()
    {
        return f1;
    }

    public void setF1(String f1)
    {
        this.f1 = f1;
    }

    @Override
    public String toString()
    {
        return "Stub1 [f1=" + f1 + ", getInt=" + getInt + "]";
    }
}
