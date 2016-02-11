package com.artkostm.core.configuration.internal;

/**
 * 
 * @author Artsiom_Chuiko
 *
 */
public class NettyConfig
{
    private String host;
    private int port;
    
    public NettyConfig()
    {}
    
    public NettyConfig(final String host, final int port)
    {
        super();
        this.host = host;
        this.port = port;
    }

    public String getHost()
    {
        return host;
    }
    
    public int getPort()
    {
        return port;
    }
    
    @Override
    public String toString()
    {
        return "Netty [host=" + host + ", port=" + port + "]";
    }
}
