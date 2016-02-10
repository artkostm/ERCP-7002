package com.artkostm.core.web.network.router;

import com.artkostm.core.web.network.router.Router;

final class StringRouter 
{
    // Utility classes should not have a public or default constructor.
    private StringRouter() 
    { }

    public static final Router<String> router = new Router<String>()
              .GET("/articles",             "index")
              .GET("/articles/:id",         "show")
              .GET("/articles/:id/:format", "show")
        .GET_FIRST("/articles/new",         "new")
             .POST("/articles",             "post")
            .PATCH("/articles/:id",         "patch")
           .DELETE("/articles/:id",         "delete")
              .ANY("/anyMethod",            "anyMethod")
              .GET("/download/:*",          "download")
         .notFound("404");

    // Visualize the routes only once
    static 
    {
        System.out.println(router.toString());
    }
}

interface Action 
{ }
class Index implements Action 
{ }
class Show  implements Action 
{ }
