package com.artkostm.configurator;

import java.io.IOException;

import org.codehaus.groovy.control.CompilationFailedException;
import org.codehaus.groovy.control.CompilerConfiguration;

import com.artkostm.configurator.fs.ScriptUtils;
import com.artkostm.configurator.model.Configuration;
import com.artkostm.configurator.model.Metadata;
import com.artkostm.configurator.model.closure.RoutingRepository;
import com.artkostm.configurator.util.ConfigLogger;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import groovy.util.DelegatingScript;

public class Main 
{
    public static void main(String[] args) throws CompilationFailedException, IOException
    {
        Binding sharedData = new Binding(args);
        Metadata config = new Configuration();
        ConfigLogger logger = new ConfigLogger(config);
        RoutingRepository repo = new RoutingRepository(config, RoutingRepository.POST);
        sharedData.setVariable("name", "Artsiom");
        sharedData.setVariable(logger.name(), logger);
        sharedData.setVariable(repo.name(), repo);
        
        CompilerConfiguration cc = new CompilerConfiguration();
        cc.setScriptBaseClass(DelegatingScript.class.getName());
        GroovyShell shell = new GroovyShell(Main.class.getClassLoader(), sharedData, cc);
        String script = ScriptUtils.getScript("classpath:configuration.gr");
        DelegatingScript delegating = (DelegatingScript) shell.parse(script);
        delegating.setDelegate(config);
        delegating.run();
        System.out.println(repo.getRepository());
    }
    
    public static class Route
    {
        private String method;
        private String url;
        private String controller;
        
        public Route(String method, String url, String controller)
        {
            this.method = method;
            this.url = url;
            this.controller = controller;
        }

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
