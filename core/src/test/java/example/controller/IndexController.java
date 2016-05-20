package example.controller;

import java.util.HashMap;
import java.util.Map;

import com.artkostm.core.akka.actors.ControllerActor;
import com.artkostm.core.akka.http.message.HttpMessage;
import com.artkostm.core.web.controller.Result;

public class IndexController extends ControllerActor
{
    @Override
    protected Result onRequest(HttpMessage msg) throws Exception
    {
        Map<String, Object> root = new HashMap<String, Object>();
        root.put("name", "Artsiom");
        System.out.println();
        return ok(view("index.html", root)).asHtml();
    }
}
