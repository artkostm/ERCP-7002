package com.artkostm.targa.script.engine;

import java.util.ArrayList;
import java.util.List;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;

public class ConfigurationScriptEngineFactory implements ScriptEngineFactory
{
    public static final String ENGINE_NAME = "Configuration Script";
    public static final String ENGINE_EXTENSION_NAME = "ACScript";
    public static final String ENGINE_VERSION = "1.0.0";
    
    @Override
    public String getEngineName() 
    {
        return ENGINE_NAME;
    }

    @Override
    public String getEngineVersion() 
    {
        return ENGINE_VERSION;
    }

    @Override
    public List<String> getExtensions() 
    {
        final List<String> extList = new ArrayList<String>();
        extList.add(ENGINE_EXTENSION_NAME);
        return extList;
    }

    @Override
    public List<String> getMimeTypes() 
    {
        final List<String> extList = new ArrayList<String>();
        extList.add("code/" + ENGINE_EXTENSION_NAME);
        return extList;
    }

    @Override
    public List<String> getNames() 
    {
        final List<String> extList = new ArrayList<String>();
        extList.add(ENGINE_EXTENSION_NAME);
        return extList;
    }

    @Override
    public String getLanguageName() 
    {
        return "Web routers configuration scripting language";
    }

    @Override
    public String getLanguageVersion() 
    {
        return "1.0.0";
    }

    @Override
    public Object getParameter(String key) 
    {
        if (key == ScriptEngine.ENGINE)
            return this.getEngineName();
        else if (key == ScriptEngine.ENGINE_VERSION)
            return this.getEngineVersion();
        else if (key == ScriptEngine.NAME)
            return this.getNames();
        else if (key == ScriptEngine.LANGUAGE)
            return this.getLanguageName();
        else if (key == ScriptEngine.LANGUAGE_VERSION)
            return this.getLanguageVersion();
        else
            return null;
    }

    @Override
    public String getMethodCallSyntax(String obj, String m, String... args) 
    {
        return null;
    }

    @Override
    public String getOutputStatement(String toDisplay) 
    {
        return "System.out.print(" + toDisplay + ")";
    }

    @Override
    public String getProgram(String... statements) 
    {
        return null;
    }

    @Override
    public ScriptEngine getScriptEngine() 
    {
        return new ConfigurationScriptEngine();
    }
}
