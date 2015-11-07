package by.artkostm.di.metadata.impl;

import java.lang.reflect.Method;

import rx.Observable;
import by.artkostm.di.filter.StaticMethodFilter;
import by.artkostm.di.metadata.ConfigurationMetadata;

public class ConfigurationMetadataImpl implements ConfigurationMetadata
{
    private final Object configurationObject;
    private final String name;
    private final Role role;

    public ConfigurationMetadataImpl(Object configurationObject, String name, Role role)
    {
        super();
        this.configurationObject = configurationObject;
        this.name = name;
        this.role = role;
    }

    @Override
    public Object getObject()
    {
        return configurationObject;
    }

    @Override
    public Role getRole()
    {
        return role;
    }

    @Override
    public String getName()
    {
        return name;
    }

    @Override
    public Class<?> getType()
    {
        return configurationObject.getClass();
    }

    /**
     *  always return null;
     */
    @Override
    public Method getFactoryMethod()
    {
        return null;
    }

    /**
     *  always return null;
     */
    @Override
    public Method getInitMethod()
    {
        return null;
    }

    /**
     *  always return null;
     */
    @Override
    public Method getDestroyMethod()
    {
        return null;
    }

    /**
     *  always return null;
     */
    @Override
    public Method getPostConstructMethod()
    {
        return null;
    }

    /**
     *  always return null;
     */
    @Override
    public Method getPreDestroyMethod()
    {
        return null;
    }

    @Override
    public Observable<Method> getFactoryMethods()
    {
        final Class<?> clazz = getType();
        final Method[] methods = clazz.getDeclaredMethods();
        return Observable.from(methods).filter(new StaticMethodFilter());
    }
}
