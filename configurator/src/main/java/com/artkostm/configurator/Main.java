package com.artkostm.configurator;

import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.codehaus.groovy.control.CompilationFailedException;
import org.codehaus.groovy.control.CompilerConfiguration;

import groovy.lang.Binding;
import groovy.lang.Closure;
import groovy.lang.GroovyShell;
import groovy.util.DelegatingScript;

public class Main 
{
    public static void main(String[] args) throws CompilationFailedException, IOException
    {
        Binding sharedData = new Binding();
        CompilerConfiguration cc = new CompilerConfiguration();
        cc.setScriptBaseClass(DelegatingScript.class.getName());
        GroovyShell shell = new GroovyShell(Main.class.getClassLoader(), sharedData, cc);
        String script = IOUtils.toString(Main.class.getClassLoader().getResourceAsStream("configuration.gr"));
        DelegatingScript delegating = (DelegatingScript) shell.parse(script);
        Config config = new Config();
        delegating.setDelegate(config);
        delegating.run();
        //System.out.println(config);
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
    
    public static interface HiddenCall
    {
        public Object doCall(Object...args);
    }
    
    public static class Print extends Closure<Object> implements HiddenCall
    {
        /**
         * 
         */
        private static final long serialVersionUID = 1L;
        
        public Print(Object owner) {
            super(owner);
        }
        @Override
        public Object doCall(Object...args) {
            if (args != null)
            {
                for (Object obj : args)
                {
                    System.out.println("Hello, "+obj);
                }
            }
            return Closure.DONE;
        }
    }
    public static class Config
    {
        private String msg;
        
        private Print show;
        
        public Print getShow() {
            return show;
        }

        public void setShow(Print show) {
            this.show = show;
        }

        public void apply(Closure<?> closure) throws IllegalArgumentException, IllegalAccessException
        {
            closure.setDelegate(route);
            closure.call();
        }
        
        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }
        
        public void msg(String msg) {
            this.msg = msg;
        }

        public Route getRoute() {
            return route;
        }

        public void setRoute(Route route) {
            this.route = route;
        }

        public Config() {
            show = new Print(this);
        }

        private Route route = new Route();

        @Override
        public String toString() {
            return "Config [msg=" + msg + ", route=" + route + "]";
        }
    }
}
