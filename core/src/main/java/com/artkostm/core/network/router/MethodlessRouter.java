package com.artkostm.core.network.router;

import io.netty.util.internal.StringUtil;

/**
 * Router that contains information about route matching orders, but doesn't
 * contain information about HTTP request methods.
 *
 * Routes are devided into 3 sections: "first", "last", and "other".
 * Routes in "first" are matched first, then in "other", then in "last".
 */
final class MethodlessRouter<T>
{
    private final OrderlessRouter<T> first = new OrderlessRouter<T>();
    private final OrderlessRouter<T> other = new OrderlessRouter<T>();
    private final OrderlessRouter<T> last = new OrderlessRouter<T>();

    //--------------------------------------------------------------------------

    /** Returns the "first" router; routes in this router will be matched first. */
    public OrderlessRouter<T> first()
    {
        return first;
    }

    /**
     * Returns the "other" router; routes in this router will be matched after those in the "first" router, but before
     * those in the "last" router.
     */
    public OrderlessRouter<T> other()
    {
        return other;
    }

    /** Returns the "last" router; routes in this router will be matched last. */
    public OrderlessRouter<T> last()
    {
        return last;
    }

    /** Returns the number of routes in this router. */
    public int size()
    {
        return first.routes().size() + other.routes().size() + last.routes().size();
    }

    //--------------------------------------------------------------------------

    /**
     * Adds route to the "first" section.
     *
     * A path can only point to one target. This method does nothing if the path has already been added.
     */
    public MethodlessRouter<T> addRouteFirst(final String path, final T target)
    {
        first.addRoute(path, target);
        return this;
    }

    /**
     * Adds route to the "other" section.
     *
     * A path can only point to one target. This method does nothing if the path has already been added.
     */
    public MethodlessRouter<T> addRoute(final String path, final T target)
    {
        other.addRoute(path, target);
        return this;
    }

    /**
     * Adds route to the "last" section.
     *
     * A path can only point to one target. This method does nothing if the path has already been added.
     */
    public MethodlessRouter<T> addRouteLast(final String path, final T target)
    {
        last.addRoute(path, target);
        return this;
    }

    //--------------------------------------------------------------------------

    /** Removes the route specified by the path. */
    public void removePath(final String path)
    {
        first.removePath(path);
        other.removePath(path);
        last.removePath(path);
    }

    /** Removes all routes leading to the target. */
    public void removeTarget(final T target)
    {
        first.removeTarget(target);
        other.removeTarget(target);
        last.removeTarget(target);
    }

    //--------------------------------------------------------------------------

    /** @return {@code null} if no match; note: {@code queryParams} is not set in {@link RouteResult} */
    public RouteResult<T> route(final String path)
    {
        return route(StringUtil.split(Path.removeSlashesAtBothEnds(path), '/'));
    }

    /** @return {@code null} if no match; note: {@code queryParams} is not set in {@link RouteResult} */
    public RouteResult<T> route(final String[] requestPathTokens)
    {
        RouteResult<T> ret = first.route(requestPathTokens);
        if (ret != null)
        {
            return ret;
        }

        ret = other.route(requestPathTokens);
        if (ret != null)
        {
            return ret;
        }

        ret = last.route(requestPathTokens);
        if (ret != null)
        {
            return ret;
        }

        return null;
    }

    /** Checks if there's any matching route. */
    public boolean anyMatched(final String[] requestPathTokens)
    {
        return first.anyMatched(requestPathTokens) ||
            other.anyMatched(requestPathTokens) ||
            last.anyMatched(requestPathTokens);
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
        String ret = first.path(target, params);
        if (ret != null)
        {
            return ret;
        }

        ret = other.path(target, params);
        if (ret != null)
        {
            return ret;
        }

        return last.path(target, params);
    }
}
