package by.artkostm.di.context.module;

import by.artkostm.di.context.DependencyContext;

public abstract class BindModule implements Module
{
    protected DependencyContext context;

    public DependencyContext getContext()
    {
        return context;
    }
}
