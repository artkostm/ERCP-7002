package io.netty.example.http.router;

import com.artkostm.core.web.network.router.Router;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class HttpRouterServer 
{
    public static final int PORT = 8080;

    public static void main(String[] args) throws Exception 
    {
        // This is an example router, it will be used at HttpRouterServerHandler.
        //
        // For simplicity of this example, route targets are just simple strings.
        // But you can make them classes, and at HttpRouterServerHandler once you
        // get a target class, you can create an instance of it and dispatch the
        // request to the instance etc.
        Router<String> router = new Router<String>()
            .GET("/",             "Index page")
            .GET("/",             "Index page2")
            .POST("/",             "POST REQUEST1")
            .POST("/",             "POST REQUEST2")
            .GET("/articles/:id", "Article show page")
            .GET("/articles/:id/show", "Article show page2")
            .GET("/articles/:id/:format", "Format is SHOW")
            .notFound("404 Not Found");
        System.out.println(router);

        NioEventLoopGroup bossGroup   = new NioEventLoopGroup(1);
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        try 
        {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
             .childOption(ChannelOption.TCP_NODELAY, java.lang.Boolean.TRUE)
             .childOption(ChannelOption.SO_KEEPALIVE, java.lang.Boolean.TRUE)
             .channel(NioServerSocketChannel.class)
             .childHandler(new HttpRouterServerInitializer(router));

            Channel ch = b.bind(PORT).sync().channel();
            System.out.println("Server started: http://127.0.0.1:" + PORT + '/');

            ch.closeFuture().sync();
        } 
        finally 
        {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
