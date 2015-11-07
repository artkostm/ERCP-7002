package by.artkostm.di.metadata;

import java.lang.reflect.Method;

public interface LifeCycleMetadata
{
    public Object getObject();
    public Role getRole();
    public String getName();
    public Class<?> getType();
    public Method getFactoryMethod();
    public Method getInitMethod();
    public Method getDestroyMethod();
    public Method getPostConstructMethod();
    public Method getPreDestroyMethod();
    
    public enum Role
    {
        Bean, Singleton, Configuration;
    }
}
