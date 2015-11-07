package by.artkostm.di.util;

import java.lang.reflect.Field;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class FieldInjector
{
    private static final Logger LOG = LogManager.getLogger(FieldInjector.class);
    
    public FieldInjector()
    {}
        
    public Object injectField(Object obj, Field field, Object value)
    {
        Object object = null;
        field.setAccessible(true);
        try
        {
            field.set(obj, value);
            object = obj;
        }
        catch (IllegalArgumentException | IllegalAccessException e)
        {
            LOG.warn("Cannot inject field " + field.getName() + " for object " + obj);
        }
        finally
        {
            field.setAccessible(false);
        }
        return object;
    }
}
