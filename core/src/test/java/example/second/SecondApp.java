package example.second;

import com.artkostm.core.web.ApplicationBootstrap;
import com.artkostm.core.web.WebApplicationAdapter;

/**
 * Created by arttsiom.chuiko on 30/05/16.
 */
public class SecondApp extends WebApplicationAdapter
{
    public static void main(String[] args)
    {
        ApplicationBootstrap.run(SecondApp.class, "/secondconfig.conf");
    }
}