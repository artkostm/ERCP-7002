package com.artkostm.core.controller;

public class ControllerForTest extends Controller
{
    public static Result index()
    {
        return ok(view("index.html")).asHtml();
    }
}
