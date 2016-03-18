package com.artkostm.core.akka;

import io.netty.handler.codec.http.HttpMethod;


import java.util.Map;
import java.util.function.BiConsumer;

import com.artkostm.core.web.network.router.Router;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigObject;
import com.typesafe.config.ConfigValue;

@SuppressWarnings("unchecked")
public class RouterFactory implements RouterProvider<Class<?>>
{    
   
    @Override
    public Router<Class<?>> get(final Config config)
    {
        final Router<Class<?>> router = new Router<Class<?>>();
        final ConfigObject root = config.root();
        ConfigValue routesConfig = root.get("routes");
        Map<String, Object> configs = (Map<String, Object>) routesConfig.unwrapped();
        configs.forEach(new RouterInflater<Object, Class<?>>(router, 
                in -> 
                {
                    if (in instanceof String) return Class.forName((String) in);
                    else throw new RuntimeException("Can't create route with " + in);
                }
        ));
        return router;
    }

    public static class RouterInflater<In, Out> implements BiConsumer<String, In>
    {
        private final Router<Out> router;
        
        private final TypeConverter<? extends Out> converter;
        
        final TypeConverter<Out> defaultConverter = new TypeConverter<Out>()
        {
            @Override
            public Out convert(Object in)
            {
                return (Out) in;
            }
         };
        
        public RouterInflater(final Router<Out> router)
        {
            this.router = router;
            this.converter = defaultConverter;
        }
        
        public RouterInflater(final Router<Out> router, TypeConverter<? extends Out> converter)
        {
            this.router = router;
            this.converter = converter;
        }
        
        @Override
        public void accept(String uri, Object u) 
        {
            final Map<String, Object> classes = (Map<String, Object>) u;
            final String[] path = uri.split("\\s");
            final HttpMethod method = HttpMethod.valueOf(path[0].trim());
            final String url = path[1].trim();
            try
            {
                final Out out = converter.convert(classes.get("class"));
                router.addRoute(method, url, out);
            }
            catch (Exception e) {}
        }
    }
    
    public static interface TypeConverter<Out>
    {
        Out convert(Object in) throws Exception;
    }
}
