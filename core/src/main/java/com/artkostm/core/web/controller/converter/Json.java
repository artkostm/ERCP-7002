package com.artkostm.core.web.controller.converter;

import java.io.IOException;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectWriter;
import org.codehaus.jackson.node.ArrayNode;
import org.codehaus.jackson.node.ObjectNode;

/**
 * 
 * @author Artsiom_Chuiko
 *
 */
public class Json
{
    private static final ObjectMapper defaultObjectMapper = newDefaultMapper();
    private static volatile ObjectMapper objectMapper = null;

    public static ObjectMapper newDefaultMapper()
    {
        final ObjectMapper mapper = new ObjectMapper();
//        mapper.registerModule(new Jdk8Module());
//        mapper.registerModule(new JavaTimeModule());
//        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return mapper;
    }

    /**
     * Get the ObjectMapper used to serialize and deserialize objects to and from JSON values.
     *
     * This can be set to a custom implementation using Json.setObjectMapper.
     *
     * @return the ObjectMapper currently being used
     */
    public static ObjectMapper mapper()
    {
        if (objectMapper == null)
        {
            return defaultObjectMapper;
        }
        else
        {
            return objectMapper;
        }
    }

    private static String generateJson(final Object o, final boolean prettyPrint, final boolean escapeNonASCII)
    {
        try
        {
            final ObjectWriter writer = mapper().writer();
//            if (prettyPrint)
//            {
//                writer = writer.with(SerializationFeature.INDENT_OUTPUT);
//            }
//            if (escapeNonASCII)
//            {
//                writer = writer.with(Feature.ESCAPE_NON_ASCII);
//            }
            return writer.writeValueAsString(o);
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    /**
     * Convert an object to JsonNode.
     *
     * @param data Value to convert in Json.
     */
    public static JsonNode toJson(final Object data)
    {
        try
        {
            return mapper().valueToTree(data);
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

    /**
     * Convert a JsonNode to a Java value
     *
     * @param json Json value to convert.
     * @param clazz Expected Java value type.
     */
    public static <A> A fromJson(final JsonNode json, Class<A> clazz)
    {
        try
        {
            return mapper().treeToValue(json, clazz);
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

    /**
     * Creates a new empty ObjectNode.
     */
    public static ObjectNode newObject()
    {
        return mapper().createObjectNode();
    }

    /**
     * Creates a new empty ArrayNode.
     */
    public static ArrayNode newArray()
    {
        return mapper().createArrayNode();
    }

    /**
     * Convert a JsonNode to its string representation.
     */
    public static String stringify(final JsonNode json)
    {
        return generateJson(json, false, false);
    }

    /**
     * Convert a JsonNode to its string representation, escaping non-ascii characters.
     */
    public static String asciiStringify(final JsonNode json)
    {
        return generateJson(json, false, true);
    }

    /**
     * Convert a JsonNode to its string representation.
     */
    public static String prettyPrint(final JsonNode json)
    {
        return generateJson(json, true, false);
    }

    /**
     * Parse a String representing a json, and return it as a JsonNode.
     */
    public static JsonNode parse(final String src)
    {
        try
        {
            return mapper().readTree(src);
        }
        catch (Throwable t)
        {
            throw new RuntimeException(t);
        }
    }

    /**
     * Parse a InputStream representing a json, and return it as a JsonNode.
     */
    public static JsonNode parse(final java.io.InputStream src)
    {
        try
        {
            return mapper().readTree(src);
        }
        catch (Throwable t)
        {
            throw new RuntimeException(t);
        }
    }

    /**
     * Parse a byte array representing a json, and return it as a JsonNode.
     */
    public static JsonNode parse(final byte[] src)
    {
        try
        {
            return mapper().readTree(src);
        }
        catch (Throwable t)
        {
            throw new RuntimeException(t);
        }
    }

    /**
     * Inject the object mapper to use.
     *
     * This is intended to be used when Play starts up. By default, Play will inject its own object mapper here, but
     * this mapper can be overridden either by a custom plugin or from Global.onStart.
     */
    public static void setObjectMapper(final ObjectMapper mapper)
    {
        objectMapper = mapper;
    }

}
