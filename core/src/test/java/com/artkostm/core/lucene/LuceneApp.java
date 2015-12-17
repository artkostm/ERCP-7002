package com.artkostm.core.lucene;

import com.artkostm.configurator.model.Metadata;
import com.artkostm.core.WebApplication;

public class LuceneApp extends WebApplication
{

    @Override
    public Metadata configure() 
    {
        return configurator().createConfiguration("classpath:luceneapp.gr");
    }

    public static void main(String[] args)
    {
        new LuceneApp().run();
    }
}
