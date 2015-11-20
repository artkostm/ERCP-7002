package com.artkostm.configurator.fs;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.apache.commons.io.IOUtils;

public class ScriptUtils
{
    public static final String CLASSPATH_PREFIX = "classpath:";
    public static final String EMPTY_STRING = "";
    
    public static String getScript(final String fileName) throws IOException
    {
        if (isClasspathResource(fileName))
        {
            final String name = fileName.replace(CLASSPATH_PREFIX, EMPTY_STRING);
            return IOUtils.toString(ClassLoader.getSystemResourceAsStream(name));
        }
        final File file = new File(fileName);
        final FileReader reader = new FileReader(file);
        return IOUtils.toString(reader);
    }
    
    protected static boolean isClasspathResource(final String fileName)
    {
        return fileName.startsWith(CLASSPATH_PREFIX);
    }
}
