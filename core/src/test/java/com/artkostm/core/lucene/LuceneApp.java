package com.artkostm.core.lucene;

import com.artkostm.core.web.ApplicationBootstrap;
import com.artkostm.core.web.WebApplicationAdapter;

public class LuceneApp extends WebApplicationAdapter
{
    private static final String CONFIG_PATH = "classpath:luceneapp.gr";
    
    public static void main(String[] args)
    {
        ApplicationBootstrap.run(LuceneApp.class, CONFIG_PATH);
    }
}
