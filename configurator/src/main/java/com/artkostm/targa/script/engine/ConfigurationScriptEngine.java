package com.artkostm.targa.script.engine;

import java.io.Reader;

import javax.script.AbstractScriptEngine;
import javax.script.Bindings;
import javax.script.Compilable;
import javax.script.CompiledScript;
import javax.script.Invocable;
import javax.script.ScriptContext;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptException;

public class ConfigurationScriptEngine extends AbstractScriptEngine implements Compilable, Invocable
{

    @Override
    public Object eval(String script, ScriptContext context)
            throws ScriptException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Object eval(Reader reader, ScriptContext context)
            throws ScriptException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Bindings createBindings() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ScriptEngineFactory getFactory() 
    {
        return new ConfigurationScriptEngineFactory();//TODO: change to one-to-one create logic
    }

    @Override
    public Object invokeMethod(Object thiz, String name, Object... args)
            throws ScriptException, NoSuchMethodException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Object invokeFunction(String name, Object... args)
            throws ScriptException, NoSuchMethodException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public <T> T getInterface(Class<T> clasz) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public <T> T getInterface(Object thiz, Class<T> clasz) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public CompiledScript compile(String script) throws ScriptException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public CompiledScript compile(Reader script) throws ScriptException {
        // TODO Auto-generated method stub
        return null;
    }

}
