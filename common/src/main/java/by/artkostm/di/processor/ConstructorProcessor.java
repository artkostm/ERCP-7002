package by.artkostm.di.processor;

import java.lang.reflect.Constructor;

import by.artkostm.di.filter.ConstructorFilter;
import rx.Observable;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class ConstructorProcessor implements Func1<Class<?>, Observable<Constructor<?>>>
{

    @Override
    public Observable<Constructor<?>> call(Class<?> clazz)
    {
        return Observable.from(clazz.getConstructors())
            .filter(new ConstructorFilter()).subscribeOn(Schedulers.io());
    }
}
