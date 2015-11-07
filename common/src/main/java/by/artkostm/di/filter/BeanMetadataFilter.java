package by.artkostm.di.filter;

import by.artkostm.di.metadata.LifeCycleMetadata;
import by.artkostm.di.metadata.LifeCycleMetadata.Role;
import rx.functions.Func1;

public class BeanMetadataFilter implements Func1<LifeCycleMetadata, Boolean>
{
    @Override
    public Boolean call(LifeCycleMetadata t)
    {
        return t.getRole() == Role.Bean || t.getRole() == Role.Singleton;
    }
}
