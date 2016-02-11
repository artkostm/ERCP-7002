package com.artkostm.core.configuration.internal;

/**
 * 
 * @author Artsiom_Chuiko
 *
 */
public class TemplateConfig
{
    private String directory;
    
    public TemplateConfig()
    {}

    public TemplateConfig(final String directory)
    {
        super();
        this.directory = directory;
    }

    public String getDirectory()
    {
        return directory;
    }

    @Override
    public String toString()
    {
        return "Template [directory=" + directory + "]";
    }
}
