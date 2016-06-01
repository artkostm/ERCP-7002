package example.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.artkostm.core.akka.actors.ControllerActor;
import com.artkostm.core.akka.http.message.AppExternalMessage;
import com.artkostm.core.akka.http.message.HttpMessage;
import com.artkostm.core.web.controller.Result;

import scala.util.Try;

public class IndexController extends ControllerActor
{
    @Override
    protected Result onRequest(HttpMessage msg) throws Exception
    {
        Map<String, Object> root = new HashMap<String, Object>();
        root.put("name", "Man");
        final Try<Object> result = call(context().actorSelection("akka.tcp://server@127.0.0.1:2552/user/second"), 
            new AppExternalMessage("Hello from server:2552"), 2, TimeUnit.SECONDS);
        root.put("result", result);
        return ok(view("index.html", root)).asHtml();
    }
}
