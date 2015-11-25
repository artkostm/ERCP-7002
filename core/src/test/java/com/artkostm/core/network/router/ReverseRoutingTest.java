package com.artkostm.core.network.router;

import java.util.HashMap;
import java.util.Map;

import static io.netty.handler.codec.http.HttpMethod.*;

import static org.junit.Assert.*;

import org.junit.Assert;
import org.junit.Test;

public class ReverseRoutingTest 
{
    @Test
    public void testHandleMethod() 
    {
        Assert.assertEquals("/articles", StringRouter.router.path(GET, "index"));

        Assert.assertEquals("/articles/123", StringRouter.router.path(GET, "show", "id", "123"));

        Assert.assertEquals("/anyMethod", StringRouter.router.path(GET, "anyMethod"));
        Assert.assertEquals("/anyMethod", StringRouter.router.path(POST, "anyMethod"));
        Assert.assertEquals("/anyMethod", StringRouter.router.path(PUT, "anyMethod"));
    }

    @Test
    public void testHandleEmptyParams() 
    {
        Assert.assertEquals("/articles", StringRouter.router.path("index"));
    }

    @Test
    public void testHandleMapParams() 
    {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", 123);
        Assert.assertEquals("/articles/123", StringRouter.router.path("show", map));
    }

    @Test
    public void testHandleVarargs() 
    {
        Assert.assertEquals("/download/foo/bar.png", StringRouter.router.path("download", "*", "foo/bar.png"));
    }

    @Test
    public void testReturnPathWithMinimumNumberOfParams() 
    {
        Map<String, Object> map1 = new HashMap<String, Object>();
        map1.put("id",     123);
        map1.put("format", "json");
        Assert.assertEquals("/articles/123/json", StringRouter.router.path("show", map1));

        Map<String, Object> map2 = new HashMap<String, Object>();
        map2.put("id",     123);
        map2.put("format", "json");
        map2.put("x",      1);
        map2.put("y",      2);
        String path = StringRouter.router.path("show", map2);
        boolean matched1 = path.equals("/articles/123/json?x=1&y=2");
        boolean matched2 = path.equals("/articles/123/json?y=2&x=1");
        assertEquals(true, matched1 || matched2);
    }
}
