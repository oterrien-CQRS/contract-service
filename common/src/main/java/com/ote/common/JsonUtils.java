package com.ote.common;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.util.List;

@NoArgsConstructor
public final class JsonUtils {

    // NB: ObjectMapper is threadsafe
    private static final ObjectMapper MAPPER;

    static {
        MAPPER = new ObjectMapper();
        MAPPER.setPropertyNamingStrategy(PropertyNamingStrategy.LOWER_CAMEL_CASE);
        MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public static <T> T parse(String content, Class<T> type) throws IOException {
        return MAPPER.readValue(content, type);
    }

    public static <T> List<T> parseFromJsonList(String content, Class<T> genericType) throws IOException {
        return MAPPER.readValue(content, MAPPER.getTypeFactory().constructCollectionType(List.class, genericType));
    }

    public static <T> String serialize(T obj) throws IOException {
        return MAPPER.writeValueAsString(obj);
    }
}