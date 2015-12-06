package com.artkostm.template;

import java.util.HashMap;

public class Main
{
    public static void main(String[] args)
    {
        TemplateCompiller.configure("src/main/resources/pages");
        final String result = TemplateCompiller.compileTemplate("index.html", new HashMap<String, Object>());
        System.out.println(result);
    }
}
