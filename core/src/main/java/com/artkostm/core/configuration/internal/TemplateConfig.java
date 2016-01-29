package com.artkostm.core.configuration.internal;

public class TemplateConfig
{
    private String directory;

    public String getDirectory()
    {
        return directory;
    }

    public void setDirectory(String directory)
    {
        this.directory = directory;
    }

    @Override
    public String toString()
    {
        return "Template [directory=" + directory + "]";
    }
}
