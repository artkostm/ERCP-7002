package com.artkostm.template;

import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

public class Main
{
    public static void main(String[] args) throws IOException, TemplateException
    {
        Configuration config = new Configuration(Configuration.VERSION_2_3_23);
        config.setDirectoryForTemplateLoading(new File("src/main/resources/pages"));
        config.setDefaultEncoding("UTF-8");
        config.setTemplateExceptionHandler(TemplateExceptionHandler.HTML_DEBUG_HANDLER);
        Template temp = config.getTemplate("test/text.html");
        Writer out = new OutputStreamWriter(System.out);
        Map<String, Object> root = new HashMap<String, Object>();
        root.put("name", "Artsiom");
        temp.process(root, out);
        out.close();
    }
}
