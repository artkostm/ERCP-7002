package by.artkostm.di.filter;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;

import rx.functions.Func1;

public class ConstructorFilter implements Func1<Constructor<?>, Boolean>{

    @Override
    public Boolean call(Constructor<?> constructor) {
        final boolean isDefault = constructor.getParameterTypes().length == 0;
        final boolean isPrivate = constructor.getModifiers() == Modifier.PRIVATE 
                || constructor.getModifiers() == Modifier.PROTECTED;
        return !isPrivate && !isDefault;
    }
}
