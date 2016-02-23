package com.artkostm.core.akka.extension.router;

import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.QueryStringDecoder;
import io.netty.util.internal.StringUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import akka.actor.UntypedActor;

public class Router<T> extends UntypedActor
{
    private final Map<HttpMethod, MethodlessRouter<T>> routers =
        new HashMap<HttpMethod, MethodlessRouter<T>>();

    private final MethodlessRouter<T> anyMethodRouter =
        new MethodlessRouter<T>();

    private T notFound;
    
    @Override
    public void onReceive(Object msg) throws Exception
    {
        
    }

    //--------------------------------------------------------------------------
    // Design decision:
    // We do not allow access to routers and anyMethodRouter, because we don't
    // want to expose MethodlessRouter, OrderlessRouter, and Path.
    // Exposing those will complicate the use of this package.

    /**
     * Returns the fallback target for use when there's no match at {@link #route(HttpMethod, String)}.
     */
    public T notFound()
    {
        return notFound;
    }

    /** Returns the number of routes in this router. */
    public int size()
    {
        int ret = anyMethodRouter.size();

        for (MethodlessRouter<T> router : routers.values())
        {
            ret += router.size();
        }

        return ret;
    }

    //--------------------------------------------------------------------------

    /**
     * Add route to the "first" section.
     *
     * A path can only point to one target. This method does nothing if the path has already been added.
     */
    public Router<T> addRouteFirst(final HttpMethod method, final String path, final T target)
    {
        getMethodlessRouter(method).addRouteFirst(path, target);
        return this;
    }

    /**
     * Add route to the "other" section.
     *
     * A path can only point to one target. This method does nothing if the path has already been added.
     */
    public Router<T> addRoute(final HttpMethod method, final String path, final T target)
    {
        getMethodlessRouter(method).addRoute(path, target);
        return this;
    }

    /**
     * Add route to the "last" section.
     *
     * A path can only point to one target. This method does nothing if the path has already been added.
     */
    public Router<T> addRouteLast(final HttpMethod method, final String path, final T target)
    {
        getMethodlessRouter(method).addRouteLast(path, target);
        return this;
    }

    /**
     * Sets the fallback target for use when there's no match at {@link #route(HttpMethod, String)}.
     */
    public Router<T> notFound(final T target)
    {
        this.notFound = target;
        return this;
    }

    private MethodlessRouter<T> getMethodlessRouter(final HttpMethod method)
    {
        if (method == null)
        {
            return anyMethodRouter;
        }

        MethodlessRouter<T> r = routers.get(method);
        if (r == null)
        {
            r = new MethodlessRouter<T>();
            routers.put(method, r);
        }

        return r;
    }

    //--------------------------------------------------------------------------

    /** Removes the route specified by the path. */
    public void removePath(final String path)
    {
        for (MethodlessRouter<T> r : routers.values())
        {
            r.removePath(path);
        }
        anyMethodRouter.removePath(path);
    }

    /** Removes all routes leading to the target. */
    public void removeTarget(final T target)
    {
        for (MethodlessRouter<T> r : routers.values())
        {
            r.removeTarget(target);
        }
        anyMethodRouter.removeTarget(target);
    }

    //--------------------------------------------------------------------------

    /**
     * If there's no match, returns the result with {@link #notFound(Object) notFound} as the target if it is set,
     * otherwise returns {@code null}.
     */
    public RouteResult<T> route(final HttpMethod method, final String uri)
    {
        final QueryStringDecoder decoder = new QueryStringDecoder(uri);
        final String[] tokens = StringUtil.split(Path.removeSlashesAtBothEnds(decoder.path()), '/');

        MethodlessRouter<T> router = routers.get(method);
        if (router == null)
        {
            router = anyMethodRouter;
        }

        RouteResult<T> ret = router.route(tokens);
        if (ret != null)
        {
            return new RouteResult<T>(ret.target(), ret.pathParams(), decoder.parameters());
        }

        if (router != anyMethodRouter)
        {
            ret = anyMethodRouter.route(tokens);
            if (ret != null)
            {
                return new RouteResult<T>(ret.target(), ret.pathParams(), decoder.parameters());
            }
        }

        if (notFound != null)
        {
            // Return mutable map to be consistent, instead of
            // Collections.<String, String>emptyMap()
            return new RouteResult<T>(notFound, new HashMap<String, String>(), decoder.parameters());
        }

        return null;
    }

    //--------------------------------------------------------------------------
    // For implementing OPTIONS and CORS.

    /**
     * Returns allowed methods for a specific URI.
     *
     * For {@code OPTIONS *}, use {@link #allAllowedMethods()} instead of this method.
     */
    public Set<HttpMethod> allowedMethods(final String uri)
    {
        final QueryStringDecoder decoder = new QueryStringDecoder(uri);
        final String[] tokens = StringUtil.split(Path.removeSlashesAtBothEnds(decoder.path()), '/');

        if (anyMethodRouter.anyMatched(tokens))
        {
            return allAllowedMethods();
        }

        final Set<HttpMethod> ret = new HashSet<HttpMethod>(routers.size());
        for (Map.Entry<HttpMethod, MethodlessRouter<T>> entry : routers.entrySet())
        {
            final MethodlessRouter<T> router = entry.getValue();
            if (router.anyMatched(tokens))
            {
                final HttpMethod method = entry.getKey();
                ret.add(method);
            }
        }

        return ret;
    }

    /** Returns all methods that this router handles. For {@code OPTIONS *}. */
    public Set<HttpMethod> allAllowedMethods()
    {
        if (anyMethodRouter.size() > 0)
        {
            final Set<HttpMethod> ret = new HashSet<HttpMethod>(9);
            ret.add(HttpMethod.CONNECT);
            ret.add(HttpMethod.DELETE);
            ret.add(HttpMethod.GET);
            ret.add(HttpMethod.HEAD);
            ret.add(HttpMethod.OPTIONS);
            ret.add(HttpMethod.PATCH);
            ret.add(HttpMethod.POST);
            ret.add(HttpMethod.PUT);
            ret.add(HttpMethod.TRACE);
            return ret;
        }
        else
        {
            return new HashSet<HttpMethod>(routers.keySet());
        }
    }

    //--------------------------------------------------------------------------

    /**
     * Given a target and params, this method tries to do the reverse routing and returns the path.
     *
     * The params are put to placeholders in the path. The params can be a map of {@code placeholder name -> value} or
     * ordered values. If a param doesn't have a placeholder, it will be put to the query part of the path.
     *
     * @return {@code null} if there's no match
     */
    public String path(final HttpMethod method, final T target, final Object... params)
    {
        MethodlessRouter<T> router = (method == null) ? anyMethodRouter : routers.get(method);

        // Fallback to anyMethodRouter if no router is found for the method
        if (router == null)
        {
            router = anyMethodRouter;
        }

        final String ret = router.path(target, params);
        if (ret != null)
        {
            return ret;
        }

        // Fallback to anyMethodRouter if the router was not anyMethodRouter and no path is found
        return (router != anyMethodRouter) ? anyMethodRouter.path(target, params) : null;
    }

    /**
     * Given a target and params, this method tries to do the reverse routing and returns the path.
     *
     * The params are put to placeholders in the path. The params can be a map of {@code placeholder name -> value} or
     * ordered values. If a param doesn't have a placeholder, it will be put to the query part of the path.
     *
     * @return {@code null} if there's no match
     */
    public String path(final T target, final Object... params)
    {
        final Collection<MethodlessRouter<T>> rs = routers.values();
        for (MethodlessRouter<T> r : rs)
        {
            final String ret = r.path(target, params);
            if (ret != null)
            {
                return ret;
            }
        }
        return anyMethodRouter.path(target, params);
    }

    //--------------------------------------------------------------------------

    /** Returns visualized routing rules. */
    @Override
    public String toString()
    {
        // Step 1/2: Dump routers and anyMethodRouter in order
        final int numRoutes = size();
        final List<String> methods = new ArrayList<String>(numRoutes);
        final List<String> paths = new ArrayList<String>(numRoutes);
        final List<String> targets = new ArrayList<String>(numRoutes);

        // For router
        for (Entry<HttpMethod, MethodlessRouter<T>> e : routers.entrySet())
        {
            HttpMethod method = e.getKey();
            MethodlessRouter<T> router = e.getValue();
            aggregateRoutes(method.toString(), router.first().routes(), methods, paths, targets);
            aggregateRoutes(method.toString(), router.other().routes(), methods, paths, targets);
            aggregateRoutes(method.toString(), router.last().routes(), methods, paths, targets);
        }

        // For anyMethodRouter
        aggregateRoutes("*", anyMethodRouter.first().routes(), methods, paths, targets);
        aggregateRoutes("*", anyMethodRouter.other().routes(), methods, paths, targets);
        aggregateRoutes("*", anyMethodRouter.last().routes(), methods, paths, targets);

        // For notFound
        if (notFound != null)
        {
            methods.add("*");
            paths.add("*");
            targets.add(targetToString(notFound));
        }

        // Step 2/2: Format the List into aligned columns: <method> <path> <target>
        int maxLengthMethod = maxLength(methods);
        int maxLengthPath = maxLength(paths);
        String format = "%-" + maxLengthMethod + "s  %-" + maxLengthPath + "s  %s\n";
        int initialCapacity = (maxLengthMethod + 1 + maxLengthPath + 1 + 20) * methods.size();
        StringBuilder b = new StringBuilder(initialCapacity);
        for (int i = 0; i < methods.size(); i++)
        {
            String method = methods.get(i);
            String path = paths.get(i);
            String target = targets.get(i);
            b.append(String.format(format, method, path, target));
        }
        return b.toString();
    }

    /** Helper for toString */
    private static <T> void aggregateRoutes(
        final String method, final Map<Path, T> routes,
        final List<String> accMethods, final List<String> accPaths, final List<String> accTargets)
    {
        for (Map.Entry<Path, T> entry : routes.entrySet())
        {
            accMethods.add(method);
            accPaths.add("/" + entry.getKey().path());
            accTargets.add(targetToString(entry.getValue()));
        }
    }

    /** Helper for toString */
    private static int maxLength(final List<String> coll)
    {
        int max = 0;
        for (String e : coll)
        {
            int length = e.length();
            if (length > max)
            {
                max = length;
            }
        }
        return max;
    }

    /**
     * Helper for toString; for example, returns "io.netty.example.http.router.HttpRouterServerHandler" instead of
     * "class io.netty.example.http.router.HttpRouterServerHandler"
     */
    private static String targetToString(final Object target)
    {
        if (target instanceof Class)
        {
            final String className = ((Class<?>) target).getName();
            return className;
        }
        else
        {
            return target.toString();
        }
    }

    //--------------------------------------------------------------------------

    public Router<T> CONNECT(final String path, final T target)
    {
        return addRoute(HttpMethod.CONNECT, path, target);
    }

    public Router<T> DELETE(final String path, final T target)
    {
        return addRoute(HttpMethod.DELETE, path, target);
    }

    public Router<T> GET(final String path, final T target)
    {
        return addRoute(HttpMethod.GET, path, target);
    }

    public Router<T> HEAD(final String path, final T target)
    {
        return addRoute(HttpMethod.HEAD, path, target);
    }

    public Router<T> OPTIONS(final String path, final T target)
    {
        return addRoute(HttpMethod.OPTIONS, path, target);
    }

    public Router<T> PATCH(final String path, final T target)
    {
        return addRoute(HttpMethod.PATCH, path, target);
    }

    public Router<T> POST(final String path, final T target)
    {
        return addRoute(HttpMethod.POST, path, target);
    }

    public Router<T> PUT(final String path, final T target)
    {
        return addRoute(HttpMethod.PUT, path, target);
    }

    public Router<T> TRACE(final String path, final T target)
    {
        return addRoute(HttpMethod.TRACE, path, target);
    }

    public Router<T> ANY(final String path, final T target)
    {
        return addRoute(null, path, target);
    }

    //--------------------------------------------------------------------------

    public Router<T> CONNECT_FIRST(final String path, final T target)
    {
        return addRouteFirst(HttpMethod.CONNECT, path, target);
    }

    public Router<T> DELETE_FIRST(final String path, final T target)
    {
        return addRouteFirst(HttpMethod.DELETE, path, target);
    }

    public Router<T> GET_FIRST(final String path, final T target)
    {
        return addRouteFirst(HttpMethod.GET, path, target);
    }

    public Router<T> HEAD_FIRST(final String path, final T target)
    {
        return addRouteFirst(HttpMethod.HEAD, path, target);
    }

    public Router<T> OPTIONS_FIRST(final String path, final T target)
    {
        return addRouteFirst(HttpMethod.OPTIONS, path, target);
    }

    public Router<T> PATCH_FIRST(final String path, final T target)
    {
        return addRouteFirst(HttpMethod.PATCH, path, target);
    }

    public Router<T> POST_FIRST(final String path, final T target)
    {
        return addRouteFirst(HttpMethod.POST, path, target);
    }

    public Router<T> PUT_FIRST(final String path, final T target)
    {
        return addRouteFirst(HttpMethod.PUT, path, target);
    }

    public Router<T> TRACE_FIRST(final String path, final T target)
    {
        return addRouteFirst(HttpMethod.TRACE, path, target);
    }

    public Router<T> ANY_FIRST(final String path, final T target)
    {
        return addRouteFirst(null, path, target);
    }

    //--------------------------------------------------------------------------

    public Router<T> CONNECT_LAST(final String path, final T target)
    {
        return addRouteLast(HttpMethod.CONNECT, path, target);
    }

    public Router<T> DELETE_LAST(final String path, final T target)
    {
        return addRouteLast(HttpMethod.DELETE, path, target);
    }

    public Router<T> GET_LAST(final String path, final T target)
    {
        return addRouteLast(HttpMethod.GET, path, target);
    }

    public Router<T> HEAD_LAST(final String path, final T target)
    {
        return addRouteLast(HttpMethod.HEAD, path, target);
    }

    public Router<T> OPTIONS_LAST(final String path, final T target)
    {
        return addRouteLast(HttpMethod.OPTIONS, path, target);
    }

    public Router<T> PATCH_LAST(final String path, final T target)
    {
        return addRouteLast(HttpMethod.PATCH, path, target);
    }

    public Router<T> POST_LAST(final String path, final T target)
    {
        return addRouteLast(HttpMethod.POST, path, target);
    }

    public Router<T> PUT_LAST(final String path, final T target)
    {
        return addRouteLast(HttpMethod.PUT, path, target);
    }

    public Router<T> TRACE_LAST(final String path, final T target)
    {
        return addRouteLast(HttpMethod.TRACE, path, target);
    }

    public Router<T> ANY_LAST(final String path, final T target)
    {
        return addRouteLast(null, path, target);
    }
}
