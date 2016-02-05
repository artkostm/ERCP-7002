package com.artkostm.core.web.network.handler;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.artkostm.core.web.controller.Context;
import com.artkostm.core.web.controller.ControllerMethodInvoker;
import com.artkostm.core.web.controller.Result;
import com.artkostm.core.web.network.handler.content.BodyConsumer;
import com.artkostm.core.web.network.handler.content.SimpleContentTypeResolver;
import com.artkostm.core.web.network.handler.method.processor.HttpMethodProcessor;
import com.artkostm.core.web.network.handler.method.processor.HttpMethodProcessorFacade;
import com.artkostm.core.web.network.handler.util.HttpContentReader;
import com.artkostm.core.web.network.handler.util.responsewriter.WriterPipeline;
import com.artkostm.core.web.network.handler.util.responsewriter.pipeline.DefaultHeadersSetter;
import com.artkostm.core.web.network.handler.util.responsewriter.pipeline.LastStageStub;
import com.artkostm.core.web.network.handler.util.responsewriter.pipeline.ResponseBuilder;
import com.artkostm.core.web.network.handler.util.responsewriter.pipeline.ResponseWriterPipeline;
import com.artkostm.core.web.network.router.RouteResult;
import com.artkostm.core.web.network.router.Router;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultHttpResponse;
import io.netty.handler.codec.http.DefaultLastHttpContent;
import io.netty.handler.codec.http.HttpChunkedInput;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.LastHttpContent;
import io.netty.handler.stream.ChunkedStream;
import io.netty.util.ReferenceCountUtil;

public class HttpServerHandler extends SimpleChannelInboundHandler<HttpObject>
{    
    private final HttpMethodProcessor<?> methodFacade;
    private HttpRequest req;
    private Map<String, List<String>> attributesMap;
    private ByteBuf contentBuffer;
    private final SimpleContentTypeResolver contentTypeResolver;
    private final Router<List<Method>> router;
    private RouteResult<List<Method>> routeResult;
    private boolean decoded;
    private final WriterPipeline<Object, HttpResponse> pipeline;
    
    public HttpServerHandler(final Router<List<Method>> router)
    {
        methodFacade = new HttpMethodProcessorFacade();
        contentBuffer = Unpooled.buffer();
        contentTypeResolver = new SimpleContentTypeResolver();
        this.router = router;
        pipeline = new ResponseWriterPipeline()
            .add(new ResponseBuilder())
            .add(new DefaultHeadersSetter())
            .add(new LastStageStub());
    }
    
    @Override
    protected void channelRead0(final ChannelHandlerContext ctx, final HttpObject msg) throws Exception
    {
        if (msg instanceof HttpRequest) 
        {
            final Context context = new Context();
            attributesMap = new HashMap<String, List<String>>();
            req = (HttpRequest) msg;
            methodFacade.process(msg, context);
            if (req.getMethod().equals(HttpMethod.GET))
            {
                writeResponse(ctx);
                reset();
                return;
            }
            decoded = true;
        }
        
        if (msg instanceof HttpContent && decoded)
        {
            try
            {
                methodFacade.process(msg, Context.current());
                consumer.chunk((HttpContent) msg);
                if (msg instanceof LastHttpContent)
                {
                    writeResponse(ctx);
                    consumer.finished();
                    return;
                }
            }
            catch (Exception e)
            {
                consumer.handleError(e);
                writeResponse(ctx);
                reset();
                return;
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
    
    private final BodyConsumer consumer = new BodyConsumer() 
    {
        @Override
        public void handleError(final Throwable t) 
        {
            //TODO: add logging here            
        }
        
        @Override
        public void finished() 
        {
            reset();
        }
        
        @Override
        public void chunk(final HttpContent content) 
        {
            if (req != null)
            {
                final String contentTypeString = req.headers().get(HttpHeaders.Names.CONTENT_TYPE);
                final boolean isFormData = req != null &&
                        (contentTypeResolver.resolveType(contentTypeString, FORM_DATA) ||
                         contentTypeResolver.resolveType(contentTypeString, FORM_URLENCODED));
                HttpContentReader.readAttributes(attributesMap, contentBuffer, content, req, isFormData);
            }
        }
    };
    
    public static final String FORM_DATA = "form-data";
    public static final String FORM_URLENCODED = "x-www-form-urlencoded";
    
    private void reset()
    {
        req = null;
        decoded = false;
        attributesMap = null;
        contentBuffer = null;
        routeResult = null;
        ReferenceCountUtil.release(contentBuffer);
        contentBuffer = Unpooled.buffer();
    }
    
    private void writeResponse(final ChannelHandlerContext ctx)
    {
        routeResult = router.route(req.getMethod(), req.getUri());
        Context.current().setPathParams(routeResult.pathParams());
        //TODO: set content
        Context.current().setContent(HttpContentReader.read(contentBuffer));
        final Result result = ControllerMethodInvoker.invoke(routeResult.target());
        final HttpResponse response = new DefaultHttpResponse(HttpVersion.HTTP_1_1, 
                result == null ? HttpResponseStatus.NO_CONTENT : HttpResponseStatus.OK, true);

        response.headers().set(HttpHeaders.Names.TRANSFER_ENCODING, HttpHeaders.Values.CHUNKED);
        response.headers().set(HttpHeaders.Names.CONTENT_TYPE, result != null ? result.getContentType() : "text/plain");

        // Disable cache by default
        response.headers().set(HttpHeaders.Names.CACHE_CONTROL, "no-cache, no-store, must-revalidate");
        response.headers().set(HttpHeaders.Names.PRAGMA, "no-cache");
        response.headers().set(HttpHeaders.Names.EXPIRES, "0");
        response.headers().set(HttpHeaders.Names.SERVER, "My Web Server/0.0.1");
        //TODO: set cookies
        //response.headers().set(HttpHeaders.Names.SET_COOKIE, ServerCookieEncoder.STRICT.encode(Context.current().getCookies().get("MYSESSIONID")));
        if (result.getContentLength() >= 0) 
        {
            response.headers().set(HttpHeaders.Names.CONTENT_LENGTH, result.getContentLength());
        }
        
        if (HttpHeaders.is100ContinueExpected(req)) 
        {
            //ctx.write(new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.CONTINUE));
        }
        final boolean keepAlive = HttpHeaders.isKeepAlive(req);

        if (keepAlive) 
        {
            response.headers().set(HttpHeaders.Names.CONNECTION, HttpHeaders.Values.KEEP_ALIVE);
            //response.headers().set(HttpHeaders.Names.CONNECTION, HttpHeaders.Values.KEEP_ALIVE);
            //ctx.write(response);
        } 
        else
        {
            response.headers().set(HttpHeaders.Names.CONNECTION, HttpHeaders.Values.CLOSE);
            //ctx.write(response).addListener(ChannelFutureListener.CLOSE);
        }
        
        ctx.write(response);
        
        if (result.getStream() != null)
        {
            ctx.write(new HttpChunkedInput(new ChunkedStream(result.getStream())));
        }
        
        LastHttpContent fs = new DefaultLastHttpContent();
        ChannelFuture sendContentFuture = ctx.writeAndFlush(fs);
        if (!HttpHeaders.isKeepAlive(req)) 
        {
            sendContentFuture.addListener(ChannelFutureListener.CLOSE);
        }
    }
}
