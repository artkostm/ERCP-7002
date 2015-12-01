package com.artkostm.core.network.handler;

import com.artkostm.core.network.handler.content.BodyConsumer;
import com.artkostm.core.network.handler.method.processor.HttpMethodProcessor;
import com.artkostm.core.network.handler.method.processor.HttpMethodProcessorFacade;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.LastHttpContent;

public class HttpServerHandler extends SimpleChannelInboundHandler<HttpObject>
{    
    private final HttpMethodProcessor methodFacade;
    
    private final BodyConsumer comsumer = new BodyConsumer() 
    {    
        private final ByteBuf contentBuf = Unpooled.buffer();
        @Override
        public void handleError(Throwable t) 
        {
            t.printStackTrace();
        }
        
        @Override
        public void finished() 
        {
            System.out.println("FINISHED:\n");
            final byte[] bytes = new byte[contentBuf.readableBytes()];
            contentBuf.readBytes(bytes);
            System.out.println(new String(bytes));
        }
        
        @Override
        public void chunk(HttpContent content) 
        {
            contentBuf.writeBytes(content.content());
        }
    };
    
    public HttpServerHandler()
    {
        methodFacade = new HttpMethodProcessorFacade();
    }
    
    @Override
    protected void channelRead0(final ChannelHandlerContext ctx, final HttpObject msg) throws Exception
    {
        if (msg instanceof HttpRequest) 
        {
            final HttpRequest req = (HttpRequest) msg;
            
            methodFacade.process(req);

            if (HttpHeaders.is100ContinueExpected(req)) 
            {
                ctx.write(new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.CONTINUE));
            }
            final boolean keepAlive = HttpHeaders.isKeepAlive(req);
//            final FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, Unpooled.wrappedBuffer(CONTENT.getBytes()));
//            response.headers().set(HttpHeaders.Names.CONTENT_TYPE, "application/json");
//            response.headers().set(HttpHeaders.Names.CONTENT_LENGTH, response.content().readableBytes());
//            response.headers().set(HttpHeaders.Names.SERVER, "ArtApp/Version 0.0.1");

            if (!keepAlive) 
            {
                //ctx.write(response).addListener(ChannelFutureListener.CLOSE);
            } 
            else 
            {
                //response.headers().set(HttpHeaders.Names.CONNECTION, HttpHeaders.Values.KEEP_ALIVE);
                //ctx.write(response);
            }
        }
        
        if (msg instanceof HttpContent)
        {
            comsumer.chunk((HttpContent)msg);
            if (msg instanceof LastHttpContent)
            {
                comsumer.finished();
            }
        }
    }
    
    @Override
    public void channelReadComplete(final ChannelHandlerContext ctx) throws Exception
    {
        super.channelReadComplete(ctx);
        ctx.flush();
    }

    @Override
    public void exceptionCaught(final ChannelHandlerContext ctx, final Throwable cause) throws Exception
    {
        super.exceptionCaught(ctx, cause);
        ctx.close();
    }
}
