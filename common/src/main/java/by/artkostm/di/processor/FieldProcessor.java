package by.artkostm.di.processor;

import java.lang.reflect.Field;

import by.artkostm.di.filter.FieldFilter;
import rx.Observable;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class FieldProcessor implements Func1<Class<?>, Observable<Field>>
{

    @Override
    public Observable<Field> call(Class<?> clazz)
    {
        return Observable.from(clazz.getDeclaredFields())
            .filter(new FieldFilter()).subscribeOn(Schedulers.io());
    }
}
