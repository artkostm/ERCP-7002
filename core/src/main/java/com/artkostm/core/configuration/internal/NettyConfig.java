package com.artkostm.core.configuration.internal;

public class NettyConfig
{
    private String host;
    private int port;
    
    public String getHost()
    {
        return host;
    }
    
    public void setHost(String host)
    {
        this.host = host;
    }
    
    public int getPort()
    {
        return port;
    }
    
    public void setPort(int port)
    {
        this.port = port;
    }
    
    @Override
    public String toString()
    {
        return "Netty [host=" + host + ", port=" + port + "]";
    }
}
