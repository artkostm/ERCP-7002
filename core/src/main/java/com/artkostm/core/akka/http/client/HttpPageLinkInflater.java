package com.artkostm.core.akka.http.client;

import java.util.StringTokenizer;

public class HttpPageLinkInflater
{
    public static void main(String[] args)
    {
        final String input = 
                "<meta charset=\"UTF-8\"><title>Netty.docs: User guide for 5.x</title><meta content=\"width=device-width, initial-scale=1.0\" name=\"viewport\">"+
                "<link href=\"../images/favicon.ico\" rel=\"shortcut icon\"><link href=\"http://feeds.feedburner.com/netty_project\" rel=\"alternate\" title=\"News Feed\" type=\"application/rss+xml\">"+
                "<style>body{padding-top:60px;}</style><link href=\"http://netdna.bootstrapcdn.com/bootstrap/3.2.0/css/bootstrap.min.css\" media=\"screen\" rel=\"stylesheet\" type=\"text/css\">"+
                "<link href=\"http://netdna.bootstrapcdn.com/font-awesome/4.0.3/css/font-awesome.min.css\" media=\"screen\" rel=\"stylesheet\" type=\"text/css\">"+
                "<script src=\"../lib/sh/scripts/shCore.js\" type=\"text/javascript\"></script>"+
                "<script src=\"../lib/sh/scripts/shBrushXml.js\" type=\"text/javascript\"></script>"+
                "<link href=\"../lib/sh/styles/shCore.css\" rel=\"stylesheet\" type=\"text/css\">"+
                "<link href=\"../lib/sh/styles/shThemeDefault.css\" rel=\"stylesheet\" type=\"text/css\">"+
                "<link href=\"../lib/common.css\" rel=\"stylesheet\" type=\"text/css\">"+
                "<script src=\"../lib/common.js\" type=\"text/javascript\"></script>"+
                "<!--[if lt IE 9]>"+
                "      <script src=\"http://cdnjs.cloudflare.com/ajax/libs/html5shiv/3.7/html5shiv.js\" type=\"text/javascript\"></script>"+
                "      <script src=\"http://cdnjs.cloudflare.com/ajax/libs/respond.js/1.3.0/respond.js\" type=\"text/javascript\"></script>"+
                "    <![endif]-->"+
                "</head>"+
                "<body>"+
                "<a class=\"sr-only\" href=\"#content\" id=\"top\">Skip navigation</a>"+
                "<nav class=\"navbar navbar-default navbar-fixed-top hidden-print\" id=\"header\" role=\"navigation\">"+
                "<div class=\"container\">";
        
//        final String pattern = "(<link.*>)?";
//        Pattern p = Pattern.compile(pattern);
//        Matcher m = p.matcher(input);
//        StringBuffer sb = new StringBuffer();
//        
////        m.appendReplacement(sb, "#TAG#");
////        m.appendTail(sb);
//        System.out.println(m.replaceAll(""));
        to(input);
        
    }
    
    private static void to(String in)
    {
        String prefix = "http://netty.io";
        String[] start = {"src=\"", "href=\""};
        StringTokenizer tokenizer = new StringTokenizer(in);
        while (tokenizer.hasMoreTokens())
        {
            String str = tokenizer.nextToken();
            String link = null;
            if (str.startsWith(start[0]))
            {
                link = str.substring(5, str.indexOf("\"", 6));
                in.replace(link, prefix.concat(link));
            }
            if (str.startsWith(start[1]))
            {
                link = str.substring(6, str.indexOf("\"", 7));
                in.replace(link, prefix.concat(link));
            }
        }
        System.out.println(in);
    }
}
