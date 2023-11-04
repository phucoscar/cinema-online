package com.phucvukimcore.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;

public class JsonUtil {
    private static final ObjectMapper mapper = new ObjectMapper();

    public static String toJsonString(Object o) {
        try {
            mapper.setSerializationInclusion(JsonInclude.Include.ALWAYS);
            return mapper.writeValueAsString(o);
        } catch (JsonProcessingException e) {
            return "";
        }
    }

    public static <T> T parseObject(String json, Class<T> clazz) {
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        try {
            return mapper.readValue(json, clazz);
        } catch (JsonProcessingException e) {
            return null;
        }
    }


    public static <T> List<T> parseArray(String json, Class<T> clazz) {
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        JavaType javaType = mapper.getTypeFactory().constructParametricType(ArrayList.class, clazz);
        try {
            return mapper.readValue(json, javaType);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}
