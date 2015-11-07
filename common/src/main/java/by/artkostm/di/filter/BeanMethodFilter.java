package by.artkostm.di.filter;

import java.lang.reflect.Method;

import by.artkostm.di.annotation.Bean;

public class BeanMethodFilter extends MethodFilter
{
    @Override
    public Boolean call(Method t)
    {
        final boolean isBeanAnnotaitionPresent = t.isAnnotationPresent(Bean.class) && super.call(t); 
        return isBeanAnnotaitionPresent;
    }
}
