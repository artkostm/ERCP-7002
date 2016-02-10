package com.artkostm.core.web.network.router;

import io.netty.handler.codec.http.HttpMethod;
import io.netty.util.internal.ObjectUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/** Result of calling {@link Router#route(HttpMethod, String)}. 
 * 
 * @author Artsiom_Chuiko 
 */
public class RouteResult<T>
{
    private final T target;
    private final Map<String, String> pathParams;
    private final Map<String, List<String>> queryParams;

    /** The maps will be wrapped in Collections.unmodifiableMap. */
    public RouteResult(final T target, final Map<String, String> pathParams, final Map<String, List<String>> queryParams)
    {
        this.target = ObjectUtil.checkNotNull(target, "target");
        this.pathParams = Collections.unmodifiableMap(ObjectUtil.checkNotNull(pathParams, "pathParams"));
        this.queryParams = Collections.unmodifiableMap(ObjectUtil.checkNotNull(queryParams, "queryParams"));
    }

    public T target()
    {
        return target;
    }

    /** Returns all params embedded in the request path. */
    public Map<String, String> pathParams()
    {
        return pathParams;
    }

    /** Returns all params in the query part of the request URI. */
    public Map<String, List<String>> queryParams()
    {
        return queryParams;
    }

    //----------------------------------------------------------------------------
    // Utilities to get params.

    /**
     * Extracts the first matching param in {@code queryParams}.
     *
     * @return {@code null} if there's no match
     */
    public String queryParam(final String name)
    {
        final List<String> values = queryParams.get(name);
        return (values == null) ? null : values.get(0);
    }

    /**
     * Extracts the param in {@code pathParams} first, then falls back to the first matching param in
     * {@code queryParams}.
     *
     * @return {@code null} if there's no match
     */
    public String param(final String name)
    {
        final String pathValue = pathParams.get(name);
        return (pathValue == null) ? queryParam(name) : pathValue;
    }

    /**
     * Extracts all params in {@code pathParams} and {@code queryParams} matching the name.
     *
     * @return Unmodifiable list; the list is empty if there's no match
     */
    public List<String> params(final String name)
    {
        final List<String> values = queryParams.get(name);
        final String value = pathParams.get(name);

        if (values == null)
        {
            return (value == null) ? Collections.<String> emptyList() : Arrays.asList(value);
        }

        if (value == null)
        {
            return Collections.unmodifiableList(values);
        }
        else
        {
            final List<String> aggregated = new ArrayList<String>(values.size() + 1);
            aggregated.addAll(values);
            aggregated.add(value);
            return Collections.unmodifiableList(aggregated);
        }
    }
}
