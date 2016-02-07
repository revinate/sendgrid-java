package com.revinate.sendgrid.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.IOException;

public final class JsonUtils {

    public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper()
            .setSerializationInclusion(JsonInclude.Include.NON_NULL)
            .setPropertyNamingStrategy(PropertyNamingStrategy.CAMEL_CASE_TO_LOWER_CASE_WITH_UNDERSCORES)
            .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    public static final ObjectMapper PRETTY_PRINT_OBJECT_MAPPER = new ObjectMapper()
            .setSerializationInclusion(JsonInclude.Include.NON_NULL)
            .setPropertyNamingStrategy(PropertyNamingStrategy.CAMEL_CASE_TO_LOWER_CASE_WITH_UNDERSCORES)
            .configure(SerializationFeature.INDENT_OUTPUT, true)
            .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

    private JsonUtils() {}

    public static <T> T fromJson(String json, Class<T> type) throws IOException {
        return OBJECT_MAPPER.readValue(json, type);
    }

    public static String toJson(Object object) throws IOException {
        return OBJECT_MAPPER.writeValueAsString(object);
    }

    public static String toPrettyPrintJson(Object object) throws IOException {
        return PRETTY_PRINT_OBJECT_MAPPER.writeValueAsString(object);
    }
}
