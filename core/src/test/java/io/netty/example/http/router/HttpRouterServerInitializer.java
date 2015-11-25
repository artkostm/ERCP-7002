package io.netty.example.http.router;

import com.artkostm.core.network.handler.BadClientSilencer;
import com.artkostm.core.network.router.Router;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;

public class HttpRouterServerInitializer extends ChannelInitializer<SocketChannel>
{
    private final HttpRouterServerHandler handler;
    private final BadClientSilencer badClientSilencer = new BadClientSilencer();

    @SuppressWarnings("unchecked")
    public HttpRouterServerInitializer(@SuppressWarnings("rawtypes") Router router)
    {
        handler = new HttpRouterServerHandler(router);
    }

    @Override
    public void initChannel(SocketChannel ch)
    {
        ch.pipeline()
            .addLast(new HttpServerCodec())
            .addLast(handler)
            .addLast(badClientSilencer);
    }
}
