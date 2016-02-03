package com.artkostm.core.web.network.handler.auth;

public interface Authenticator
{
    boolean authenticate(final Credentials cr);
}
