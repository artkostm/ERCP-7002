package com.artkostm.core.akka.http.message;

import com.artkostm.core.akka.http.HttpMethods;

public interface HttpMessage
{
    HttpMethods method();
    
}
