package by.artkostm.reflection;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public final class ReflectionUtils
{
    public static boolean isStatic(final Method method)
    {
        return Modifier.isStatic(method.getModifiers());
    }
    
    public static boolean isStatic(final Class<?> declaringClass, final String methodName, 
        final Class<?>... parameterTypes)
    {
        try
        {
            final Method method = declaringClass.getDeclaredMethod(methodName, parameterTypes);
            return Modifier.isStatic(method.getModifiers());
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }
    
    private ReflectionUtils()
    {}
}
