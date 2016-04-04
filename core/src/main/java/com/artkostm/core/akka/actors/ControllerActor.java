package com.artkostm.core.akka.actors;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.DefaultHttpResponse;
import io.netty.handler.codec.http.DefaultLastHttpContent;
import io.netty.handler.codec.http.HttpChunkedInput;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.LastHttpContent;
import io.netty.handler.stream.ChunkedStream;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Map;

import scala.Option;

import com.artkostm.core.akka.http.message.HttpMessage;
import com.artkostm.core.web.controller.Context;
import com.artkostm.core.web.controller.Result;
import com.artkostm.template.TemplateCompiller;

import akka.actor.OneForOneStrategy;
import akka.actor.SupervisorStrategy;
import akka.actor.UntypedActor;
import akka.japi.pf.DeciderBuilder;

public abstract class ControllerActor extends UntypedActor
{
    public final static SupervisorStrategy supervisorStrategy = new OneForOneStrategy(
        DeciderBuilder.match(freemarker.core.InvalidReferenceException.class, ex -> SupervisorStrategy.restart())
        .matchAny(ex -> SupervisorStrategy.restart())
        .build());
    
    @Override
    public void preRestart(Throwable reason, Option<Object> message) throws Exception
    {
        if (message.nonEmpty())
        {
            System.out.println("Reason: " + reason.getCause() + ", Msg: " + message.get());
            final Result result = internalServerError(reason.getMessage());
            mapResult(result, (HttpMessage) message.get());
            super.preRestart(reason, message);
        }
    }
    
    @Override
    public void onReceive(Object msg) throws Exception
    {
        if (msg instanceof HttpMessage)
        {
            final Result result = onRequest((HttpMessage) msg);
            mapResult(result, (HttpMessage) msg);
        }
        else 
        {
            unhandled(msg);
        }
    }
    
    protected abstract Result onRequest(HttpMessage msg) throws Exception;
    
    protected void notFound(HttpMessage msg)
    {
        final ChannelHandlerContext context = msg.context();
        final ByteBuf buf = Unpooled.copiedBuffer(NOT_FOUND_CONTENT, Charset.forName("UTF-8"));
        final DefaultFullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, 
            HttpResponseStatus.NOT_FOUND, buf);
        context.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }
    
    protected String view(final String viewName, final Map<String, Object> data)
    {
        data.put("context", context());
        final String page = TemplateCompiller.compileTemplate(viewName, data);
        return page;
    }
    
    protected String view(final String viewName)
    {
        final String page = TemplateCompiller.compileTemplate(viewName, "context", context());
        return page;
    }
    
    protected Context requestContext()
    {
        return Context.current();
    }
    
    protected  Result ok() 
    {
        return new Result().status(200);
    }

    protected  Result ok(final InputStream is) 
    {
        return new Result(is).status(200);
    }

    protected  Result ok(final byte[] bytes) 
    {
        return new Result(bytes).status(200);
    }

    protected  Result ok(final String s) 
    {
        return new Result(s).status(200);
    }

    protected  Result ok(final File f) throws IOException 
    {
        return new Result(f).status(200);
    }

    protected  Result badRequest() 
    {
        return ok().status(400);
    }

    protected Result badRequest(final InputStream is) 
    {
        return ok(is).status(400);
    }

    protected Result badRequest(final byte[] bytes) 
    {
        return ok(bytes).status(400);
    }

    protected Result badRequest(final String s) 
    {
        return ok(s).status(400);
    }

    protected Result badRequest(final File f) throws IOException 
    {
        return ok(f).status(400);
    }

    protected Result created() 
    {
        return ok().status(201);
    }

    protected Result created(final InputStream is) 
    {
        return ok(is).status(201);
    }

    protected Result created(final byte[] bytes) 
    {
        return ok(bytes).status(201);
    }

    protected Result created(final String s) 
    {
        return ok(s).status(201);
    }

    protected Result created(final File f) throws IOException 
    {
        return ok(f).status(201);
    }

    protected Result forbidden() 
    {
        return ok().status(403);
    }

    protected Result forbidden(final InputStream is) 
    {
        return ok(is).status(403);
    }

    protected Result forbidden(final byte[] bytes) 
    {
        return ok(bytes).status(403);
    }

    protected Result forbidden(final String s) 
    {
        return ok(s).status(403);
    }

    protected Result forbidden(final File f) throws IOException 
    {
        return ok(f).status(403);
    }

    protected Result nothing() 
    {
        return noContent();
    }

    protected Result noContent() 
    {
        return ok().status(204);
    }

    protected Result found(final String url) 
    {
        return ok().status(302).header("Location", url);
    }

    protected Result movedPermanently(final String url) 
    {
        return ok().status(301).header("Location", url);
    }

    protected Result redirect(final String url) 
    {
        return seeOther(url);
    }

    protected Result seeOther(final String url) 
    {
        return ok().status(303).header("Location", url);
    }

    protected Result temporaryRedirect(String url) 
    {
        return ok().status(307).header("Location", url);
    }

    protected Result internalServerError() 
    {
        return ok().status(500);
    }

    protected Result internalServerError(final InputStream is) 
    {
        return ok(is).status(500);
    }

    protected Result internalServerError(final byte[] bytes) 
    {
        return ok(bytes).status(500);
    }

    protected Result internalServerError(final String s) 
    {
        return ok(s).status(500);
    }

    protected Result internalServerError(final File f) throws IOException 
    {
        return ok(f).status(500);
    }
    
    protected Result internalServerError(final Throwable e) 
    {
        final StackTraceElement[] ste = e.getStackTrace();

        final StringBuilder sb = new StringBuilder();

        for (StackTraceElement el : ste) 
        {
            sb.append(el.toString()).append("\n");
        }
        return ok("<code><b>" + e.toString() + "</b></code><br><pre>" + sb.toString() + "</pre>").status(500);
    }

    protected Result notFound() 
    {
        return ok().status(404);
    }

    protected Result notFound(final InputStream is) 
    {
        return ok(is).status(404);
    }

    protected Result notFound(final byte[] bytes) 
    {
        return ok(bytes).status(404);
    }

    protected Result notFound(final String s) 
    {
        return ok(s).status(404);
    }

    protected Result notFound(final File f) throws IOException 
    {
        return ok(f).status(404);
    }

    protected Result unauthorized() 
    {
        return ok().status(401);
    }

    protected Result unauthorized(final InputStream is) 
    {
        return ok(is).status(401);
    }

    protected Result unauthorized(final byte[] bytes) 
    {
        return ok(bytes).status(401);
    }

    protected Result unauthorized(final String s) 
    {
        return ok(s).status(401);
    }

    protected Result unauthorized(final File f) throws IOException 
    {
        return ok(f).status(401);
    }
    
private static final String NOT_FOUND_CONTENT = "<h1>404 - Not found</h1>";
    
    private void mapResult(final Result result, final HttpMessage msg)
    {
        final ChannelHandlerContext ctx = msg.context();
        final HttpRequest req = msg.request();
        final HttpResponse response = new DefaultHttpResponse(HttpVersion.HTTP_1_1, 
            result == null ? HttpResponseStatus.NO_CONTENT : HttpResponseStatus.valueOf(result.getStatus()), true);
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
