package by.artkostm.di.util;

import java.util.List;
import java.util.Map;

import by.artkostm.di.metadata.LifeCycleMetadata;

public final class BeansUtil
{
    private BeansUtil()
    {}
    
    public static void map(final Map<String, LifeCycleMetadata> context, List<LifeCycleMetadata> metadata)
    {
        for (LifeCycleMetadata lcm : metadata)
        {
            context.put(lcm.getName(), lcm);
        }
    }
}
