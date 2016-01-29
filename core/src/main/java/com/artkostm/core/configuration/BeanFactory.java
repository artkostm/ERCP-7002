package com.artkostm.core.configuration;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;

import com.typesafe.config.Config;

public class BeanFactory
{
    private static final String FIELD_PATH_DELIM = ".";
    private static final String EMPTY_STRING = "";
    
    public static <T> T create(final Config config, final Class<T> clazz) throws Exception
    {
        return create(config, clazz, EMPTY_STRING);
    }
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static <T> T create(final Config config, final Class<T> clazz, final String path) throws Exception
    {
        final Field[] fields = clazz.getDeclaredFields();
        final T obj = clazz.newInstance();
        for (Field field : fields)
        {
            field.setAccessible(true);
            final Class<?> fieldClass = field.getType(); 
            final String name = field.getName();
            final boolean isPrimitive = fieldClass.isPrimitive();
            final String fullPath = path.isEmpty() ? name : path + FIELD_PATH_DELIM + name;
            if (config.hasPath(fullPath))
            {
                if (fieldClass == List.class)
                {
                    final ParameterizedType objectListType = (ParameterizedType) field.getGenericType();
                    final Class<?> objectListClass = (Class<?>) objectListType.getActualTypeArguments()[0];
                    final List list = new ArrayList<>();
                    for (Config c : config.getConfigList(fullPath))
                    {
                        list.add(create(c, objectListClass, EMPTY_STRING));
                    }
                    field.set(obj, list);
                } 
                else
                {
                    if (isPrimitive || fieldClass == String.class)
                    {
                        final Object primitive = config.getAnyRef(fullPath);
                        field.set(obj, primitive);
                    } 
                    else 
                    {
                        field.set(obj, create(config, fieldClass, fullPath));
                    }
                }
            }
        }
        
        return obj;
    }
}
