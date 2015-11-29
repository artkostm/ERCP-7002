package com.artkostm.configurator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.groovy.control.CompilerConfiguration;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import groovy.util.DelegatingScript;

import com.artkostm.configurator.fs.ScriptUtils;
import com.artkostm.configurator.model.Configuration;
import com.artkostm.configurator.model.Metadata;
import com.artkostm.configurator.model.RouteConfig;
import com.artkostm.configurator.model.closure.RoutingRepository;
import com.artkostm.configurator.util.ConfigLogger;

public class Configurator 
{
    private String directoryForTemplateLoading;
    private int port;
    
    /**
     * 
     * @param configFilePath - configuration file path 
     * ( example: classpath:configuration.gr)
     */
    public Metadata createConfiguration(final String configFilePath)
    {
        final Binding sharedData = prepare();
        final CompilerConfiguration cc = new CompilerConfiguration();
        cc.setScriptBaseClass(DelegatingScript.class.getName());
        final GroovyShell shell = new GroovyShell(Configurator.class.getClassLoader(), sharedData, cc);
        String script = null;
        try 
        {
            script = ScriptUtils.getScript(configFilePath);
        } catch (IOException e) 
        {
            e.printStackTrace();//TODO: add logging here
        }
        DelegatingScript delegating = (DelegatingScript) shell.parse(script);
        delegating.setDelegate(this);
        delegating.run();
        final List<RouteConfig> configs = new ArrayList<RouteConfig>();
        configs.addAll(((RoutingRepository)sharedData.getProperty(RoutingRepository.POST)).getRepository());
        configs.addAll(((RoutingRepository)sharedData.getProperty(RoutingRepository.GET)).getRepository());
        configs.addAll(((RoutingRepository)sharedData.getProperty(RoutingRepository.PUT)).getRepository());
        configs.addAll(((RoutingRepository)sharedData.getProperty(RoutingRepository.DELETE)).getRepository());
        return new Configuration(configs);
    }
    
    /**
     * TODO: decouple this method
     * @return shared data
     */
    protected Binding prepare()
    {
        final Binding sharedData = new Binding();
        final ConfigLogger logger = new ConfigLogger(this);
        final RoutingRepository repoPOST = new RoutingRepository(this, RoutingRepository.POST);
        final RoutingRepository repoGET = new RoutingRepository(this, RoutingRepository.GET);
        final RoutingRepository repoDELETE = new RoutingRepository(this, RoutingRepository.DELETE);
        final RoutingRepository repoPUT = new RoutingRepository(this, RoutingRepository.PUT);
        sharedData.setProperty(logger.name(), logger);
        sharedData.setProperty(repoPOST.name(), repoPOST);
        sharedData.setProperty(repoGET.name(), repoGET);
        sharedData.setProperty(repoDELETE.name(), repoDELETE);
        sharedData.setProperty(repoPUT.name(), repoPUT);
        return sharedData;
    }
    
    public String getDirectoryForTemplateLoading()
    {
        return directoryForTemplateLoading;
    }
    
    public int getPort()
    {
        return port;
    }
}
