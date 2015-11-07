package by.artkostm.di.context;

import java.util.HashMap;
import java.util.Map;

import by.artkostm.di.context.module.BindModule;

public class Bootstrap
{
    private static final Map<Class<? extends BindModule>, BindModule> modules 
       = new HashMap<Class<? extends BindModule>, BindModule>();
    
    public static DependencyContext module(final Class<? extends BindModule> clazz)
    {
        final BindModule module = modules.get(clazz);
        if (module == null)
        {
            try
            {
                final BindModule newModule = clazz.newInstance();
                newModule.declare();
                return newModule.getContext();
            }
            catch (Exception e)
            {
                throw new RuntimeException("Can't bind module " + clazz.getName());
            }
        }
        return module.getContext();
    }
    
    public static DependencyContext defaultModule(final String packageName)
    {
        final BindModule defaultModule = new BindModule() 
        {    
            @Override
            public void declare() 
            {
                context = new ObservableContext(packageName);
            }
        };
        modules.put(defaultModule.getClass(), defaultModule);
        defaultModule.declare();
        return defaultModule.getContext();
    }
}
