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
        final TestObject object = new TestObject();
        object.setMessage("[From server]" + new String(context().getContent()));
        object.setNum(10);
        return ok(Json.toJson(object).toString()).asJson();
    }
}
