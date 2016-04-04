package com.artkostm.template;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;

public class TemplateCompiller 
{
    private static final Configuration config = new Configuration(Configuration.VERSION_2_3_23);
    
    public static Configuration configuration()
    {
        return config;
    }
    
    public static void configure(final String templateLoadingDirectory)
    {
        try 
        {
            config.setDirectoryForTemplateLoading(new File(templateLoadingDirectory));
        } 
        catch (IOException e) 
        {
            //TODO: ad logging here
        }
        config.setDefaultEncoding("UTF-8");
        config.setTemplateExceptionHandler(TemplateExceptionHandler.HTML_DEBUG_HANDLER);
    }
    
    public static String compileTemplate(final String templateName, final String name, final Object obj)
    {
        final Map<String, Object> root = new HashMap<String, Object>();
        root.put(name, obj);
        return compileTemplate(templateName, root);
    }
    
    public static String compileTemplate(final String templateName, final Map<String, Object> root)
    {
        final StringWriter out = new StringWriter();
        try
        {
            final Template temp = config.getTemplate(templateName);
            temp.process(root, out);
            return out.toString();
        } 
        catch (Exception e) 
        {
            // TODO: add logging here
            throw new RuntimeException(out.toString(), e);
        }
        finally
        {
            if (out != null)
            {
                try 
                {
                    out.close();
                } 
                catch (IOException e) 
                {}
            }
        }
    }
}
