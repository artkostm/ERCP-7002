package test.main;

import by.artkostm.di.annotation.Bean;
import by.artkostm.di.annotation.Configuration;

@Configuration(name = "stub")
public class Configuration1
{
    private String f1;
    private String f2;

    public static class InnerStub3
    {

    }

    public Configuration1()
    {
        f1 = "f1";
        f2 = "f2";
    }

    public Configuration1(String f1)
    {
        super();
        this.f1 = f1;
    }

    public String getF1()
    {
        return f1;
    }

    public String getF2()
    {
        return f2;
    }

    public void setF1(String f1)
    {
        this.f1 = f1;
    }
    
    @Bean(name = "st1")
    public Stub1 getStub1()
    {
        return new Stub1();
    }
    
    @Bean(name = "st2", skipBody = false)
    public Stub2 getStub2()
    {
        return new Stub2(f1);
    }

    @Override
    public String toString()
    {
        return "Configuration1 [f1=" + f1 + ", f2=" + f2 + "]";
    }
}
