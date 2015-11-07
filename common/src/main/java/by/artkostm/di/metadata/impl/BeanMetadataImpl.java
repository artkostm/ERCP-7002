package by.artkostm.di.metadata.impl;

import java.lang.reflect.Method;

import by.artkostm.di.metadata.BeanMetadata;

public class BeanMetadataImpl implements BeanMetadata
{
    private final Object obj;
    private final String name;
    private final Method factoryMethod;
    private final Method initMethod;
    private final Method destroyMethod;
    private final boolean skipBody;

    public BeanMetadataImpl(Object obj, String name, Method factoryMethod, 
        Method initMethod, Method destroyMethod, boolean skipBody)
    {
        super();
        this.obj = obj;
        this.name = name;
        this.factoryMethod = factoryMethod;
        this.initMethod = initMethod;
        this.destroyMethod = destroyMethod;
        this.skipBody = skipBody;
    }

    @Override
    public Object getObject()
    {
        return obj;
    }

    @Override
    public Role getRole()
    {
        return Role.Bean;
    }

    @Override
    public String getName()
    {
        return name;
    }

    @Override
    public Class<?> getType()
    {
        return obj.getClass();
    }

    @Override
    public Method getFactoryMethod()
    {
        return factoryMethod;
    }

    @Override
    public Method getInitMethod()
    {
        return initMethod;
    }

    @Override
    public Method getDestroyMethod()
    {
        return destroyMethod;
    }

    @Override
    public Method getPostConstructMethod()
    {
        return initMethod;
    }

    @Override
    public Method getPreDestroyMethod()
    {
        return destroyMethod;
    }

    @Override
    public boolean getSkipBody()
    {
        return skipBody;
    }
}
