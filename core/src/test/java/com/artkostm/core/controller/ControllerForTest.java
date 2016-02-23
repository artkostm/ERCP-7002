package com.artkostm.core.controller;

import java.util.HashMap;
import java.util.Map;

import com.artkostm.core.web.controller.Controller;
import com.artkostm.core.web.controller.Result;
import com.artkostm.core.web.controller.converter.Json;

public class ControllerForTest extends Controller
{
//    public static Result index()
//    {
//        final String name = context().getPathParams().get("name");
//        final Map<String, Object> root = new HashMap<String, Object>();
//        root.put("name", name);
//        
//        return ok(view("index.html", root)).asHtml();
//    }
    
    public static Result index()
    {        
        return ok(Json.toJson(new Message("Hello, World!")).toString()).asJson();
    }
    
    public static Result post()
    {
        final TestObject object = new TestObject();
        object.setMessage("[From server]" + new String(context().getContent()));
        object.setNum(10);
        return ok(Json.toJson(object).toString()).asJson();
    }
    
    public static class Message
    {
        String msg;
        
        public Message(String msg) 
        {
            this.msg = msg;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }
        
        
    }
}
