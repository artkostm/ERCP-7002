package example.controller;

import java.util.HashMap;
import java.util.Map;

import org.apache.camel.Exchange;

import com.artkostm.core.akka.actors.CamelControllerActor;
import com.artkostm.core.akka.http.message.HttpMessage;
import com.artkostm.core.web.controller.Result;

import akka.camel.CamelExtension;
import akka.camel.CamelMessage;

public class ApachePageController extends CamelControllerActor
{
    @Override
    protected Result onRequest(HttpMessage msg) throws Exception
    {
        return ok((String) msg.payload());
    }

    @Override
    protected String getProducerUri()
    {
        return "http://localhost:8050/hello";
    }

    @Override
    protected String getConsumerUri()
    {
        return "file:/Users/arttsiom.chuiko/git/ERCP-7002/core/src/test/resources/?fileName=transactions.csv&noop=true";
    }
    
    @Override
    protected Object onTransformMessage(CamelMessage msg)
    {
        final String body = msg.getBodyAs(String.class, CamelExtension.get(context().system()).context());
        Map<String, String> headers = new HashMap<>();
        headers.put(Exchange.HTTP_METHOD, "POST");
        CamelMessage newMsg = msg.withHeaders(headers);
        return new CamelMessage("[" + body.replace("\n", ", ") + "]", newMsg.getHeaders());
    }
}