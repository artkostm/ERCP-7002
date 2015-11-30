package com.artkostm.core.controller;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class Results 
{
    public static Result ok() 
    {
        return new Result().status(200);
    }

    public static Result ok(final InputStream is) 
    {
        return new Result(is).status(200);
    }

    public static Result ok(final byte[] bytes) 
    {
        return new Result(bytes).status(200);
    }

    public static Result ok(final String s) 
    {
        return new Result(s).status(200);
    }

    public static Result ok(final File f) throws IOException 
    {
        return new Result(f).status(200);
    }

    public static Result badRequest() 
    {
        return ok().status(400);
    }

    public static Result badRequest(final InputStream is) 
    {
        return ok(is).status(400);
    }

    public static Result badRequest(final byte[] bytes) 
    {
        return ok(bytes).status(400);
    }

    public static Result badRequest(final String s) 
    {
        return ok(s).status(400);
    }

    public static Result badRequest(final File f) throws IOException 
    {
        return ok(f).status(400);
    }

    public static Result created() 
    {
        return ok().status(201);
    }

    public static Result created(final InputStream is) 
    {
        return ok(is).status(201);
    }

    public static Result created(final byte[] bytes) 
    {
        return ok(bytes).status(201);
    }

    public static Result created(final String s) 
    {
        return ok(s).status(201);
    }

    public static Result created(final File f) throws IOException 
    {
        return ok(f).status(201);
    }

    public static Result forbidden() 
    {
        return ok().status(403);
    }

    public static Result forbidden(final InputStream is) 
    {
        return ok(is).status(403);
    }

    public static Result forbidden(final byte[] bytes) 
    {
        return ok(bytes).status(403);
    }

    public static Result forbidden(final String s) 
    {
        return ok(s).status(403);
    }

    public static Result forbidden(final File f) throws IOException 
    {
        return ok(f).status(403);
    }

    public static Result nothing() 
    {
        return noContent();
    }

    public static Result noContent() 
    {
        return ok().status(204);
    }

    public static Result found(final String url) 
    {
        return ok().status(302).header("Location", url);
    }

    public static Result movedPermanently(final String url) 
    {
        return ok().status(301).header("Location", url);
    }

    public static Result redirect(final String url) 
    {
        return seeOther(url);
    }

    public static Result seeOther(final String url) 
    {
        return ok().status(303).header("Location", url);
    }

    public static Result temporaryRedirect(String url) 
    {
        return ok().status(307).header("Location", url);
    }

    public static Result internalServerError() 
    {
        return ok().status(500);
    }

    public static Result internalServerError(final InputStream is) 
    {
        return ok(is).status(500);
    }

    public static Result internalServerError(final byte[] bytes) 
    {
        return ok(bytes).status(500);
    }

    public static Result internalServerError(final String s) 
    {
        return ok(s).status(500);
    }

    public static Result internalServerError(final File f) throws IOException 
    {
        return ok(f).status(500);
    }
    
    public static Result internalServerError(final Exception e) 
    {
        final StackTraceElement[] ste = e.getStackTrace();

        final StringBuilder sb = new StringBuilder();

        for (StackTraceElement el : ste) 
        {
            sb.append(el.toString()).append("\n");
        }
        return ok("<code>" + e.toString() + "</code><br><pre>" + sb.toString() + "</pre>").status(500);
    }

    public static Result notFound() 
    {
        return ok().status(404);
    }

    public static Result notFound(final InputStream is) 
    {
        return ok(is).status(404);
    }

    public static Result notFound(final byte[] bytes) 
    {
        return ok(bytes).status(404);
    }

    public static Result notFound(final String s) 
    {
        return ok(s).status(404);
    }

    public static Result notFound(final File f) throws IOException 
    {
        return ok(f).status(404);
    }

    public static Result unauthorized() 
    {
        return ok().status(401);
    }

    public static Result unauthorized(final InputStream is) 
    {
        return ok(is).status(401);
    }

    public static Result unauthorized(final byte[] bytes) 
    {
        return ok(bytes).status(401);
    }

    public static Result unauthorized(final String s) 
    {
        return ok(s).status(401);
    }

    public static Result unauthorized(final File f) throws IOException 
    {
        return ok(f).status(401);
    }

//    public static Result notFoundDefault(Context ctx) {
//        return ok("");//TODO:  DefaultController.notFound(ctx);
//    }
}
