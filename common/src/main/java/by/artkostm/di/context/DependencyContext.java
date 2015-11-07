package by.artkostm.di.context;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import by.artkostm.di.metadata.LifeCycleMetadata;

public abstract class DependencyContext implements BeanFactory
{
    protected static final Logger LOG = LogManager.getLogger(DependencyContext.class);
    
    protected Map<String, LifeCycleMetadata> context;
    
    public DependencyContext()
    {
        context = new HashMap<>();
    }

    @Override
    public Object getBean(final String name)
    {
        for (String beanName : context.keySet())
        {
            if (name.equals(beanName))
            {
                final LifeCycleMetadata metadata = context.get(beanName);
                final Object bean = metadata.getObject();
                return bean;
            }
        }
        return null;
    }
    
    @Override
    public Object getBean(final Class<?> clazz)
    {
        for (LifeCycleMetadata lcm : context.values())
        {
            if (lcm.getType() == clazz)
            {
                return lcm.getObject();
            }
        }
        return null;
    }

    @Override
    public boolean containtsBean(final String name)
    {
        for (String beanName : context.keySet())
        {
            if (name.equals(beanName))
            {
                return true;
            }
        }
        return false;
    }
    
    /**
     * The method to create the context. Usage: after creating application context object;
     */
    protected abstract void createContext();
    
    protected abstract void close();
    
    /**
     * Register shutdown hook for the context
     */
    {
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable()
        { 
            @Override
            public void run()
            {
                close();
                context.clear();
                context = null;
            }
        }));
    }
}
