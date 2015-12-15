package com.artkostm.core.elasticsearch;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;

public class ElasticSearch 
{
    public static void main(String[] args) throws UnknownHostException
    {
        final Settings settings = Settings.settingsBuilder()
                .put("cluster.name", "artkostm")
                .put("node.name", "node-1").build();
        final TransportClient client = TransportClient.builder().settings(settings).build();
        try
        {
            addTransportAddresses(client);
            client.prepareIndex("people", "man", "2").setSource(createMap()).execute().actionGet();
            
            final GetResponse response = client.prepareGet("people", "man", "1").execute().actionGet();
            System.out.println(response.getSourceAsString());          
        }
        finally
        {
            if (client != null)
            {
                client.close();
            }
        }
    }
    
    private static void addTransportAddresses(final TransportClient client) throws UnknownHostException
    {
        client.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("localhost"), 9300));
    }

    private static Map<String, String> createMap()
    {
        Map<String, String> map = new HashMap<String, String>();
        map.put("name", "Artsiom");
        map.put("age", "20");
        
        return map;
    }
}
