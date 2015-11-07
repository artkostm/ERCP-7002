package by.artkostm.di.metadata.builder;

import java.lang.reflect.Method;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import by.artkostm.di.metadata.LifeCycleMetadata;
import by.artkostm.di.metadata.impl.BeanMetadataImpl;
import by.artkostm.di.util.MethodInvoker;
import by.artkostm.di.util.Reflections;

public class BeanBuilder
{
    private static final Logger LOG = LogManager.getLogger(BeanBuilder.class);
    
    public static LifeCycleMetadata build(final Method factoryMethod, final Object config, final String name, final boolean skipBody)
    {
        final Class<?> beanClass = factoryMethod.getReturnType();
        final Object object = createObject(beanClass, factoryMethod, config, skipBody);
        
        
        final LifeCycleMetadata lcm = new BeanMetadataImpl(object, name, factoryMethod, 
            Reflections.findInitMethod(beanClass), Reflections.findDestroyMethod(beanClass), skipBody);
        
        return lcm;
    }  
    
    private static Object createObject(Class<?> beanClass, Method m, Object config, boolean skipBody)
    {
        try
        {
            if (skipBody)
            {
                final Object obj = MethodInvoker.invokeConstroctor(beanClass);
                return obj;
            }
            else
            {
                final Object obj = m.invoke(config);
                return obj;
            }
        }
        catch (Exception e)
        {
            LOG.warn("Cannot create bean for class: " + beanClass.getName());
        }
        return null;
    }
}
