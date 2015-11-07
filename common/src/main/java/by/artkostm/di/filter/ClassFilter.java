package by.artkostm.di.filter;

import rx.functions.Func1;

/**
 * The ClassFilter class to filter the non-class resources 
 * 
 * @author Artsiom
 *
 */
public class ClassFilter implements Func1<Class<?>, Boolean>{

    @Override
    public Boolean call(Class<?> clazz) {
        final boolean isInterface = clazz.isInterface();
        final boolean isAnnotation = clazz.isAnnotation();
        final boolean isEnum = clazz.isEnum();
        final boolean isPrimitive = clazz.isPrimitive();
        final boolean isAnonymous = clazz.isAnonymousClass();
        final boolean isSynthetic = clazz.isSynthetic();
        
        final boolean decision = !isAnnotation && !isAnonymous 
                                              && !isEnum && !isInterface 
                                              && !isPrimitive && !isSynthetic;
        return decision;
    }
}
