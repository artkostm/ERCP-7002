package com.artkostm.core.akka.http.routing;

import com.artkostm.core.akka.actors.SystemActor;
import com.artkostm.core.akka.configuration.RouteObject;
import com.artkostm.core.akka.http.message.AppInternalMessage;
import com.artkostm.core.akka.http.message.HttpMessage;
import com.artkostm.core.web.network.router.RouteResult;
import com.artkostm.core.web.network.router.Router;

public class RouterActor extends SystemActor
{
    private final Router<RouteObject> router;
    
    public RouterActor(final Router<RouteObject> router)
    {
        this.router = router;
    }

    @Override
    protected void onRequest(HttpMessage msg) throws Exception
    {
        final RouteResult<RouteObject> result = router.route(msg.request().getMethod(), msg.request().getUri());
        if (result == null || result.target() == null)
        {
            notFound(msg);
        }
        else
        {
            final RouteObject route = result.target();
            ((AppInternalMessage) msg).setRouteResult(result);
            route.getActor().tell(msg, sender());
            context().system().eventStream().publish(msg);
        }
    }
}
