package by.artkostm.di.filter;

import by.artkostm.di.annotation.Configuration;
import by.artkostm.di.annotation.Singleton;

public class ConfigurationClassFilter extends ClassFilter
{
    @Override
    public Boolean call(Class<?> clazz)
    {
        final boolean isConfiguration = clazz.isAnnotationPresent(Configuration.class) || 
                clazz.isAnnotationPresent(Singleton.class);
        final boolean decision = super.call(clazz) && isConfiguration;
        return decision;
    }
}
