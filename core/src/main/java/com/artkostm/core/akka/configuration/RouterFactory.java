package com.artkostm.core.akka.configuration;

import io.netty.handler.codec.http.HttpMethod;

import java.util.Map;
import java.util.function.BiConsumer;

import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.routing.SmallestMailboxPool;

import com.artkostm.core.web.network.router.Router;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigObject;
import com.typesafe.config.ConfigValue;

@SuppressWarnings("unchecked")
public class RouterFactory implements RouterProvider<RouteObject>
{    
    private final ActorSystem system;
    
    public RouterFactory(final ActorSystem system)
    {
        this.system = system;
    }
    
    @Override
    public Router<RouteObject> get(final Config config)
    {
        final Router<RouteObject> router = new Router<RouteObject>();
        final ConfigObject root = config.root();
        ConfigValue routesConfig = root.get("routes");
        Map<String, Object> configs = (Map<String, Object>) routesConfig.unwrapped();
        configs.forEach(new RouterInflater<Object, RouteObject>(router, 
                in -> 
                {
                    if (in == null || in.length != 3) throw new RuntimeException("Can't create route with " + in);
                    if (in[0] instanceof String && in[2] == null) return createRouteObject(validateClass((String) in[0]), (String) in[1], 1);
                    if (in[0] instanceof String && in[2] != null) return createRouteObject(validateClass((String) in[0]), (String) in[1], (Integer) in[2]);
                    else throw new RuntimeException("Can't create route with " + in);
                }
        ));
        return router;
    }
    
    protected RouteObject createRouteObject(Class<?> clazz, String name, int spin)
    {
        final String actorName = name == null ? clazz.getSimpleName() : name;
        if (spin == 1) return new RouteObject(clazz, system.actorOf(Props.create(clazz), actorName));
        else return new RouteObject(clazz, system.actorOf(Props.create(clazz).withRouter(new SmallestMailboxPool(spin)), actorName), spin);
    }
    
    protected Class<?> validateClass(String actorClass) throws Exception 
    {

        final Class<?> clazz = Class.forName(actorClass);
        if (!UntypedActor.class.isAssignableFrom(clazz)) 
        {
            throw new IllegalArgumentException(actorClass + " not an actor class !");
        }

        return clazz;
    }

    public static class RouterInflater<In, Out> implements BiConsumer<String, In>
    {
        private final Router<Out> router;
        
        private final TypeConverter<? extends Out> converter;
        
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
                final Out out = converter.convert(classes.get("class"), classes.get("name"), classes.get("spin"));
                router.addRoute(method, url, out);
            }
            catch (Exception e) 
            {
                e.printStackTrace();
            }
        }
        
        final TypeConverter<Out> defaultConverter = new TypeConverter<Out>()
                {
                    @Override
                    public Out convert(Object... in)
                    {
                        return (Out) in[0];
                    }
                 };
    }
    
    public static interface TypeConverter<Out>
    {
        Out convert(Object... in) throws Exception;
    }
}
