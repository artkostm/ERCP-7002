package by.artkostm.di.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import by.artkostm.di.annotation.Bean;
import by.artkostm.di.annotation.Configuration;
import by.artkostm.di.annotation.Inject;
import by.artkostm.di.annotation.Singleton;

public class Reflections {
    
    public static final String SETTER_PREFIX = "set";
    
    public static String getSetterName(String fieldName)
    {
        final String firstLetter = String.valueOf(fieldName.charAt(0)).toUpperCase();
        final String setter = SETTER_PREFIX + firstLetter + fieldName.substring(1);
        return setter;
    }
    
    public static String getInjectedBeanName(Field field)
    {
        if (field.isAnnotationPresent(Inject.class))
        {
            final Inject inject = field.getAnnotation(Inject.class);
            String name = inject.name();
            if (name.isEmpty())
            {
                name = getFieldName(field);
            }
            return name;
        }
        
        return null;
    }
    
    public static String getBeanName(Method m)
    {
        if (m.isAnnotationPresent(Bean.class))
        {
            final Bean bean = m.getAnnotation(Bean.class);
            String name = bean.name();
            if (name.isEmpty())
            {
                name = m.getName();
            }
            return name;
        }
        
        return null;
    }
    
    public static boolean getSkipBody(Method m)
    {
        if (m.isAnnotationPresent(Bean.class))
        {
            final Bean bean = m.getAnnotation(Bean.class);
            final boolean skipBody = bean.skipBody();
            return skipBody;
        }
        
        return true;
    }
    
    public static String getAnnotaitedClassName(Class<?> annotaitedClass, Class<? extends Annotation> annotationClass)
    {
        if (annotaitedClass.isAnnotationPresent(annotationClass))
        {
            final Annotation annotation = annotaitedClass.getAnnotation(annotationClass);
            if (annotation instanceof Configuration)
            {
                final Configuration conf = (Configuration) annotation;
                String name = conf.name();
                if (name.isEmpty())
                {
                    name = annotaitedClass.getSimpleName() + ".Configuration";
                }
                return name;
            }
            if (annotation instanceof Singleton)
            {
                final String name = annotaitedClass.getSimpleName() + ".Singleton";
                return name;
            }
        }
        return null;
    }
    
    public static Method findDestroyMethod(final Class<?> clazz)
    {
        return findMethodAnnotatedWith(clazz, PreDestroy.class);
    }
    
    public static Method findInitMethod(final Class<?> clazz)
    {
        return findMethodAnnotatedWith(clazz, PostConstruct.class);
    }
    
    public static Method findPreDestroyMethod(final Class<?> clazz)
    {
        return findMethodAnnotatedWith(clazz, PreDestroy.class);
    }
    
    public static Method findPostConstructMethod(final Class<?> clazz)
    {
        return findMethodAnnotatedWith(clazz, PostConstruct.class);
    }
    
    public static Method findMethodAnnotatedWith(final Class<?> clazz, Class<? extends Annotation> annotationClass)
    {
        final Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods)
        {
            if (method.isAnnotationPresent(annotationClass))
            {
                return method;
            }
        }
        return null;
    }
    
    public static Method[] getStaticMethods(final Class<?> clazz)
    {
        List<Method> staticMethods = new ArrayList<>();
        for (Method method : clazz.getDeclaredMethods())
        {
            final boolean isStatic = Modifier.isStatic(method.getModifiers());
            if (isStatic)
            {
                staticMethods.add(method);
            }
        }
        
        return (Method[]) staticMethods.toArray();
    }
    
    public static List<Field> getAnnotaitedFields(Class<?> clazz, Class<? extends Annotation> annotationClass)
    {
        final Field[] fields = clazz.getDeclaredFields();
        final List<Field> annotaitedFields = new ArrayList<>();
        for (Field field : fields)
        {
            if (field.isAnnotationPresent(annotationClass))
            {
                annotaitedFields.add(field);
            }
        }
        return annotaitedFields;
    }
    
    public static String getFieldName(Field field)
    {
        return field.getName();
    }
}
