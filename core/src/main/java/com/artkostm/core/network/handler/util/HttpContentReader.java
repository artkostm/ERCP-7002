package com.artkostm.core.network.handler.util;

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.multipart.Attribute;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder.EndOfDataDecoderException;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder.ErrorDataDecoderException;
import io.netty.handler.codec.http.multipart.InterfaceHttpData;
import io.netty.handler.codec.http.multipart.InterfaceHttpData.HttpDataType;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HttpContentReader 
{
    public static boolean readAttributes(final Map<String, List<String>> attributesMap, 
            final ByteBuf content, final HttpContent httpContent, final HttpRequest request, final boolean isFormData)
    {
        if (isFormData) 
        {
            final HttpPostRequestDecoder postDecoder = new HttpPostRequestDecoder(request);

            try 
            {
                postDecoder.offer(httpContent);
            } 
            catch (ErrorDataDecoderException ex) 
            {
                return false;
            }

            readHttpDataChunkByChunk(postDecoder, attributesMap);
        } 
        else 
        {
            content.writeBytes(httpContent.content());
        }
        return true;
    }
    
    public static byte[] read(final ByteBuf buf)
    {
        final byte[] bytes = new byte[buf.readableBytes() > 0 ? buf.readableBytes() : 0];
        int i = 0;
        while (buf.isReadable()) 
        {
            bytes[i] = buf.readByte();
            i++;
        }
        return bytes;
    }
    
    private static void readHttpDataChunkByChunk(final HttpPostRequestDecoder decoder, 
            final Map<String, List<String>> attributesMap) 
    {
        try 
        {
            while (decoder.hasNext()) 
            {
                final InterfaceHttpData data = decoder.next();
                if (data != null) 
                {
                    if (data.getHttpDataType() == HttpDataType.Attribute) 
                    {
                        try 
                        {
                            final Attribute attribute = (Attribute) data;
                            final List<String> list = new ArrayList<>();
                            list.add(attribute.getValue());
                            attributesMap.put(attribute.getName(), list);
                        } 
                        catch (IOException ex) 
                        {
                            //TODO: Add logging here
                        } 
                        finally 
                        {
                            data.release();
                        }
                    }
                }
            }

        } 
        catch (EndOfDataDecoderException e) 
        { 
            // TODO:it's ok, add ligging
        }
        
    }
}
