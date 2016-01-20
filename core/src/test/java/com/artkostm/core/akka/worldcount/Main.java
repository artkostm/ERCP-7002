package com.artkostm.core.akka.worldcount;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.function.BinaryOperator;

import com.artkostm.core.akka.worldcount.model.Word;
import com.artkostm.core.akka.worldcount.server.TextUtil;

public class Main
{
    private static Map<String, Integer> finalReducedMap = new HashMap<String, Integer>();
    
    public static void main(String[] args)
    {
        long start = System.currentTimeMillis();
        final BufferedReader reader = new BufferedReader(
            new InputStreamReader(
                Thread.currentThread().getContextClassLoader().getResourceAsStream("tst.txt")));
        
        try
        {
            String line = null;
            while ((line = reader.readLine()) != null) 
            {
                List<Word> words = TextUtil.evaluateExpression(line);
                Map<String, Integer> map = reduce(words);
                aggregateInMemoryReduce(map);
            }
            System.out.println("All lines send !");
            
            System.out.println(finalReducedMap);
            System.out.println("Time is " + (System.currentTimeMillis() - start));
            System.out.println("Size is " + finalReducedMap.size());
            final Integer size = finalReducedMap.values().stream().reduce(0, new BinaryOperator<Integer>()
            {
                @Override
                public Integer apply(Integer t, Integer u)
                {
                    return t + u;
                }
            });
            System.out.println("Real size is " + size);
            System.out.println("Avverage: " + (size / finalReducedMap.size()));
            finalReducedMap.clear();
        }
        catch (Exception e)
        {
            System.err.format("Exception: %s%n", e);
        }
    }
    
    private static void aggregateInMemoryReduce(final Map<String, Integer> reducedList) 
    {

        final Iterator<String> iter = reducedList.keySet().iterator();
        while (iter.hasNext()) 
        {
            final String key = iter.next();
            if (finalReducedMap.containsKey(key)) 
            {
                final Integer count = reducedList.get(key) + finalReducedMap.get(key);
                finalReducedMap.put(key, count);
            } 
            else 
            {
                finalReducedMap.put(key, reducedList.get(key));
            }
        }
    }
    
    private static NavigableMap<String, Integer> reduce(List<Word> list) 
    {
        final NavigableMap<String, Integer> reducedMap = new ConcurrentSkipListMap<String, Integer>();

        final Iterator<Word> iter = list.iterator();
        while (iter.hasNext()) 
        {
            final Word result = iter.next();
            if (reducedMap.containsKey(result.getWord())) 
            {
                Integer value = reducedMap.get(result.getWord());
                value++;
                reducedMap.put(result.getWord(), value);
            } 
            else 
            {
                reducedMap.put(result.getWord(), Integer.valueOf(1));
            }
        }
        return reducedMap;
    }
}
