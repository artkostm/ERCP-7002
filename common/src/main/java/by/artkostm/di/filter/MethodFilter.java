package by.artkostm.di.filter;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import rx.functions.Func1;

public class MethodFilter implements Func1<Method, Boolean>{

    @Override
    public Boolean call(Method t) {
        final boolean isPrivate = Modifier.isProtected(t.getModifiers()) 
                || Modifier.isPrivate(t.getModifiers());
        return !isPrivate;
    }
}
