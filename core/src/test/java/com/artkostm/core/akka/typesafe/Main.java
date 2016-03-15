package com.artkostm.core.akka.typesafe;

import java.util.Map.Entry;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.artkostm.core.configuration.ConfigAggregator;
import com.typesafe.config.ConfigValue;

public class Main
{
    public static void main(String[] args) throws Exception
    {
//        Router<List<Method>> router = new Router<>();
//        RequestMapper.map(router, ConfigAggregator.load("/application2.conf").app());
//        System.out.println(router.toString());
        System.out.println(ConfigAggregator.load("/application2.conf").configuration().getConfig("routes").entrySet());
        
        ConfigAggregator.load("/application2.conf").configuration().getConfig("routes").entrySet().forEach(new RouteeParser());
//                new Consumer<Entry<String, ConfigValue>>() 
//        {
//            @Override
//            public void accept(Entry<String, ConfigValue> t) 
//            {
//                System.out.println(t.getValue().unwrapped().getClass());
//                System.out.println("Key: " + t.getKey() + "   -   " + t.getValue().unwrapped());
//            }
//        });
    }
    
    public static class RouteeParser implements Consumer<Entry<String, ConfigValue>>
    {
        final Pattern p = Pattern.compile("(['\"])((?:.(?!\\1))*.?)\\1");
        
        @Override
        public void accept(final Entry<String, ConfigValue> t) 
        {
            final Matcher m = p.matcher(t.getKey());
            if (m.find()) System.out.println("Value : " + m.group(2));
        }
    }
    
    public static BiConsumer<String, ConfigValue> consumer = new BiConsumer<String, ConfigValue>() 
    {
        @Override
        public void accept(String t, ConfigValue u) 
        {
            System.out.println("Key: " + t + "   -   " + u);
        }
    };
}
