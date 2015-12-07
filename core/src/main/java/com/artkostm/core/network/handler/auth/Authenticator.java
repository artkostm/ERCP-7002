package com.artkostm.core.network.handler.auth;

public interface Authenticator
{
    boolean authentificate(final Credentials cr);
}
