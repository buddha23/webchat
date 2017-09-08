package com.lld360.cnc.core.utils;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * JSON处理类
 */
public class JsonUtils {
    private static ObjectMapper mapper = new ObjectMapper();

    public static ObjectMapper getMapper(boolean defaultInclude) {
        if (defaultInclude) {
            mapper.setSerializationInclusion(JsonInclude.Include.ALWAYS);
        }
        return mapper;
    }

    public static ObjectMapper getMapper(JsonInclude.Include include) {
        mapper.setSerializationInclusion(include);
        return mapper;
    }

    // 以Include.NON_NULL的模式将Object转化为JSON字符串
    public static String toJson(Object o) {
        return toJson(o, JsonInclude.Include.NON_NULL);
    }

    // 根据指定的模式将Object转化为JSON字符串
    public static String toJson(Object o, JsonInclude.Include include) {
        mapper.setSerializationInclusion(include);
        try {
            return mapper.writeValueAsString(o);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将Json字符串转化为对象
     * 对于集合和容器，需要先设置泛型类型
     * mapper.getTypeFactory().constructParametricType()
     * mapper.getTypeFactory().constructCollectionType()
     * mapper.getTypeFactory().constructMapType()
     */
    public static <T> T fromJson(String jsonString, Class<T> clazz) {
        if (StringUtils.isNotEmpty(jsonString)) {
            try {
                return mapper.readValue(jsonString, clazz);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    // 使用Json更新对象属性
    public static <T> T updateFromJson(T object, String jsonString) {
        try {
            return mapper.readerForUpdating(object).readValue(jsonString);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return object;
    }

    // 将Object对象转化为JSONP字符串
    public static String toJsonP(String callback, Object o) {
        return toJson(new JSONPObject(callback, o));
    }

    public static <T> Map<String, T> toMap(String json) {
        try {
            return mapper.readValue(json, new TypeReference<HashMap<String, T>>() {
            });
        } catch (IOException e) {
            return null;
        }
    }
}
