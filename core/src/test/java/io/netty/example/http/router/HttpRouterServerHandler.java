/*
 * Copyright 2015 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package io.netty.example.http.router;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

import com.artkostm.core.web.controller.Context;
import com.artkostm.core.web.network.handler.content.ContentTypeResolver;
import com.artkostm.core.web.network.handler.content.SimpleContentTypeResolver;
import com.artkostm.core.web.network.router.RouteResult;
import com.artkostm.core.web.network.router.Router;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.LastHttpContent;
import io.netty.handler.codec.http.multipart.Attribute;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder.ErrorDataDecoderException;
import io.netty.handler.codec.http.multipart.InterfaceHttpData;
import io.netty.handler.codec.http.multipart.InterfaceHttpData.HttpDataType;
import io.netty.util.CharsetUtil;

@ChannelHandler.Sharable
public class HttpRouterServerHandler extends SimpleChannelInboundHandler<HttpObject> 
{
    private final Router<String> router;

    private boolean flag;

    public HttpRouterServerHandler(Router<String> router) {
        this.router = router;
        contentBuf = Unpooled.buffer();
    }
    
    private HttpRequest req;
    private ByteBuf contentBuf;
    private Map<String, List<String>> postmap;

    @Override
    public void channelRead0(ChannelHandlerContext ctx, HttpObject msg) {
        
        if (msg instanceof HttpRequest)
        {
            if (contentBuf == null)
                contentBuf = Unpooled.buffer();
            Context context = new Context();
            postmap = new TreeMap<String, List<String>>();
            req = (HttpRequest) msg;
            RouteResult<String> routeResult = router.route(req.getMethod(), req.getUri());
            context.addCookie("id", routeResult.param("id"));
            context.addCookie(UUID.randomUUID().toString(), routeResult.queryParam("name"));
            if (req.getMethod() == HttpMethod.GET)
            {
                flag = true;
            }
            if (routeResult.target() == "404 Not Found")
            {
                FullHttpResponse res = new DefaultFullHttpResponse(
                        HttpVersion.HTTP_1_1, HttpResponseStatus.NOT_FOUND,
                        Unpooled.copiedBuffer("<h1>404 Not Found<h1>", CharsetUtil.UTF_8)
                );
                ctx.writeAndFlush(res).addListener(ChannelFutureListener.CLOSE);
            }
            
            if (HttpHeaders.is100ContinueExpected(req)) {
                ctx.writeAndFlush(new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.CONTINUE));
                return;
            }
        }
        
        if (msg instanceof LastHttpContent) 
        {
            HttpResponse res = createResponse(req, router);
            if (contentBuf != null)
            {
                contentBuf.clear();
                contentBuf = null;
            }
            System.out.println(Context.current());
            flushResponse(ctx, req, res);
            return;
        }
        if (msg instanceof HttpContent)
        {
            HttpContent httpContent = (HttpContent) msg;
            if (flag) {read(httpContent); flag = false;}
        }
    }
    
    
    private void read(HttpContent httpContent)
    {
        HttpPostRequestDecoder decoder = new HttpPostRequestDecoder(req);
        ContentTypeResolver typeResolver = new SimpleContentTypeResolver();
        if (typeResolver.resolveType(req.headers().get("Content-Type"), HttpHeaders.Values.MULTIPART_FORM_DATA))
        {
            try 
            {
                decoder.offer(httpContent);
                while (decoder.hasNext()) 
                {
                    InterfaceHttpData data = decoder.next();
                    if (data != null) {
                        if (data.getHttpDataType() == HttpDataType.Attribute) {
                            try {
                                Attribute attribute = (Attribute) data;
                                List<String> list = new ArrayList<String>();
                                list.add(attribute.getValue());
                                postmap.put(attribute.getName(), list);
                            } catch (IOException ex) {
                                ex.printStackTrace();
                            } finally {
                                data.release();
                            }
                        }
                    }
                }
            } 
            catch (ErrorDataDecoderException ex) 
            {
                return;
            }
        }
        else
        {
            contentBuf.writeBytes(httpContent.content());
        }
        
    }

    private HttpResponse createResponse(HttpRequest req, Router<String> router) {
        RouteResult<String> routeResult = router.route(req.getMethod(), req.getUri());
        
        // Display debug info.
        //
        // For simplicity of this example, route targets are just simple strings.
        // But you can make them classes, and here once you get a target class,
        // you can create an instance of it and dispatch the request to the instance etc.
        StringBuilder content = new StringBuilder();
        content.append("router: \n" + router + "\n");
        content.append("req: " + req + "\n\n");
        content.append("routeResult: \n");
        content.append("target: " + routeResult.target() + "\n");
        content.append("pathParams: " + routeResult.pathParams() + "\n");
        content.append("queryParams: " + routeResult.queryParams() + "\n\n");
        //content.append("allowedMethods: " + router.allowedMethods(req.getUri()));
        if (contentBuf != null)
        {
//            System.out.println(contentBuf.readableBytes());
//            byte[] bytes = new byte[contentBuf.readableBytes()];//TODO: fail
//            contentBuf.readBytes(bytes);
//            content.append("content: \n\tpost params: " + postmap + "\n\tbinary:\n" + new String(bytes));//TODO: add content from HttpRouterServerHandler.content
//            bytes = null;
        }
        
        FullHttpResponse res = new DefaultFullHttpResponse(
                HttpVersion.HTTP_1_1, HttpResponseStatus.OK,
                Unpooled.copiedBuffer(content.toString(), CharsetUtil.UTF_8)
        );

        res.headers().set(HttpHeaders.Names.CONTENT_TYPE,   "text/plain");
        res.headers().set(HttpHeaders.Names.CONTENT_LENGTH, res.content().readableBytes());

        return res;
    }

    private static ChannelFuture flushResponse(ChannelHandlerContext ctx, HttpRequest req, HttpResponse res) {
        if (!HttpHeaders.isKeepAlive(req)) {
            return ctx.writeAndFlush(res).addListener(ChannelFutureListener.CLOSE);
        } else {
            res.headers().set(HttpHeaders.Names.CONNECTION, HttpHeaders.Values.KEEP_ALIVE);
            return ctx.writeAndFlush(res);
        }
    }
}
