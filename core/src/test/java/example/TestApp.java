package example;

import com.artkostm.core.web.ApplicationBootstrap;
import com.artkostm.core.web.WebApplicationAdapter;

public class TestApp extends WebApplicationAdapter
{      
    public static void main(String[] args)
    {
        ApplicationBootstrap.run(TestApp.class);
    }
}