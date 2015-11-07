package by.artkostm.di.processor;

import java.lang.reflect.Method;

import by.artkostm.di.filter.MethodFilter;
import rx.Observable;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class MethodProcessor implements Func1<Class<?>, Observable<Method>>
{
    @Override
    public Observable<Method> call(Class<?> clazz)
    {
        return Observable.from(clazz.getDeclaredMethods())
            .filter(new MethodFilter()).subscribeOn(Schedulers.io());
    }
}
