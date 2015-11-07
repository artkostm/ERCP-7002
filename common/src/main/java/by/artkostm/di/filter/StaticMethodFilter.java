package by.artkostm.di.filter;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class StaticMethodFilter extends MethodFilter
{
    @Override
    public Boolean call(Method t)
    {
        final boolean isStatic = Modifier.isStatic(t.getModifiers()) && super.call(t); 
        return isStatic;
    }
}
