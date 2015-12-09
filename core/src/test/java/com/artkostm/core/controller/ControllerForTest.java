package com.artkostm.core.controller;

import java.util.HashMap;
import java.util.Map;

public class ControllerForTest extends Controller
{
    public static Result index()
    {
        final String name = context().getPathParams().get("name");
        final Map<String, Object> root = new HashMap<String, Object>();
        root.put("name", name);
        
        return ok(view("index.html", root)).asHtml();
    }
    
    public static Result post()
    {        
        return ok("{\"message\":\"hello\"}").asJson();
    }
}
