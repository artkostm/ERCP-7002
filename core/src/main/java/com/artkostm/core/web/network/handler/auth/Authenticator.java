package com.artkostm.core.web.network.handler.auth;

/**
 * 
 * @author Artsiom_Chuiko
 *
 */
public interface Authenticator
{
    boolean authenticate(final Credentials cr);
}
