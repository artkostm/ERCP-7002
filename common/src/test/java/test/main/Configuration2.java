package test.main;

import by.artkostm.di.annotation.Bean;
import by.artkostm.di.annotation.Configuration;

@Configuration(name = "testconfig")
public class Configuration2
{
    @Bean(skipBody = false)
    public static Object getInt()
    {
        return 10;
    }
    
    @Bean(skipBody = false)
    public String host()
    {
        return "localhost";
    }
}
