package com.artkostm.core.network.handler.content;

public class SimpleContentTypeResolver implements ContentTypeResolver
{
    @Override
    public boolean resolveType(final String contentTypeString, final String contentType) 
    {
        if (contentTypeString == null || contentType == null || contentType.length() == 0)
        {
            return false;
        }
        
        return contentTypeString.toLowerCase().contains(contentType.toLowerCase());
    }
}
