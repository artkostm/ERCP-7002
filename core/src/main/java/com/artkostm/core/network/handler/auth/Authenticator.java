package com.artkostm.core.network.handler.auth;

public interface Authenticator
{
    boolean authenticate(final Credentials cr);
}
