package com.artkostm.core.web.controller;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author Artsiom_Chuiko
 *
 */
public class Result 
{
    private int status;
    private InputStream stream;
    private String contentType;
    private long contentLength;
    private Map<String, String> headers = new HashMap<String, String>();
    
    public Result() 
    {
        contentType = "application/octet-stream";// or anything else. TODO: move to the constants class
        contentLength = -1;
    }

    public Result(final InputStream is) 
    {
        this();
        stream = is;
    }

    public Result(final byte[] bytes) 
    {
        this(new ByteArrayInputStream(bytes));
        contentLength = bytes.length;
    }

    public Result(final String s) 
    {
        this(s.getBytes());
        contentType = "text/html";  // I assume that text/html will be more often. TODO: move to the constants class
    }

    public Result(final File f) throws IOException 
    {
        this(new FileInputStream(f));
        contentType = Files.probeContentType(f.toPath());
        contentLength = f.length();
    }

    public Result status(final int status) 
    {
        this.status = status;
        return this;
    }

    public Result contentType(final String contentType) 
    {
        this.contentType = contentType;
        return this;
    }

    public Result contentLength(final long contentLength) 
    {
        this.contentLength = contentLength;
        return this;
    }

    public Result header(final String name, final String value) 
    {
        headers.put(name, value);
        return this;
    }

    public Map<String, String> headers() 
    {
        return headers;
    }

    public int getStatus() 
    {
        return status;
    }

    public InputStream getStream() 
    {
        return stream;
    }

    public String getContentType() 
    {
        return contentType;
    }

    public long getContentLength() 
    {
        return contentLength;
    }

    public Result asText() 
    {
        this.contentType = "text/plain"; //TODO: move to the constants class
        return this;
    }

    public Result asHtml() 
    {
        this.contentType = "text/html";//TODO: move to the constants class
        return this;
    }

    public Result asJson() 
    {
        this.contentType = "application/json";//TODO: move to the constants class
        return this;
    }

    public Result asXml() 
    {
        this.contentType = "text/xml";//TODO: move to the constants class
        return this;
    }

    public Result asJavascript() 
    {
        this.contentType = "application/javascript";//TODO: move to the constants class
        return this;
    }

    public Result asCss() 
    {
        this.contentType = "text/css";//TODO: move to the constants class
        return this;
    }
}
