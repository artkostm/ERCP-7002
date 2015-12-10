package com.artkostm.core.controller;

import java.util.HashMap;
import java.util.Map;

import com.artkostm.core.controller.converter.Json;

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
        System.out.println(Json.parse(context().getContent()));
        return ok("{\"message\":\"hello\"}").asJson();
    }
}
