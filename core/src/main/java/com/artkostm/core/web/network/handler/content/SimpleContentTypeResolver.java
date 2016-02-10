package com.artkostm.core.web.network.handler.content;

/**
 * 
 * @author Artsiom_Chuiko
 *
 */
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
