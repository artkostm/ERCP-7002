package com.artkostm.configurator;

import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.codehaus.groovy.control.CompilationFailedException;
import org.codehaus.groovy.control.CompilerConfiguration;

import com.artkostm.configurator.model.Configuration;
import com.artkostm.configurator.model.Metadata;

import groovy.lang.Binding;
import groovy.lang.Closure;
import groovy.lang.GroovyShell;
import groovy.util.DelegatingScript;

public class Main 
{
    public static void main(String[] args) throws CompilationFailedException, IOException
    {
        Binding sharedData = new Binding(args);
        Route r = new Route();
        sharedData.setVariable("name", "Artsiom");
        sharedData.setVariable("route", r);
        CompilerConfiguration cc = new CompilerConfiguration();
        cc.setScriptBaseClass(DelegatingScript.class.getName());
        GroovyShell shell = new GroovyShell(Main.class.getClassLoader(), sharedData, cc);
        String script = IOUtils.toString(Main.class.getClassLoader().getResourceAsStream("configuration.gr"));
        DelegatingScript delegating = (DelegatingScript) shell.parse(script);
        Metadata config = new Configuration();
        delegating.setDelegate(config);
        delegating.run();
    }
    
    public static class Route
    {
        private String method;
        private String url;
        private String controller;
        
        public String method() {
            return method;
        }

        public void method(String method) {
            this.method = method;
        }

        public String url() {
            return url;
        }

        public void url(String url) {
            this.url = url;
        }

        public String controller() {
            return controller;
        }

        public void controller(String controller) {
            this.controller = controller;
        }

        @Override
        public String toString() {
            return "Route [method=" + method + ", url=" + url + ", controller="
                    + controller + "]";
        }
    }
}
