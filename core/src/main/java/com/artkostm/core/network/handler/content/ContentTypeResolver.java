package com.artkostm.core.network.handler.content;

/**
 * The ContentTypeResolver interface is to resolve content type from HTTP request header
 * 
 * @author Artsiom
 *
 */
public interface ContentTypeResolver 
{
    boolean resolveType(final String contentTypeString, final String contentType);
}
