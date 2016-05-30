package example.controller;

import com.artkostm.core.akka.actors.ControllerActor;
import com.artkostm.core.akka.http.message.HttpMessage;
import com.artkostm.core.web.controller.Result;

/**
 * Created by arttsiom.chuiko on 30/05/16.
 */
public class EchoController extends ControllerActor
{
    @Override
    protected Result onRequest(HttpMessage msg) throws Exception
    {
        return ok(msg.payload().toString());
    }
}
