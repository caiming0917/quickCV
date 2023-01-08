package com.caijuan.springmybatis.mapper.handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.SimpleDateFormatSerializer;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

public class FastJsonUtil {
    private static SerializeConfig config = new SerializeConfig();
    private static final SerializerFeature[] features;

    public FastJsonUtil() {
    }

    public static String toJSON(Object object) {
        return JSON.toJSONString(object, features);
    }

    public static <T> T parse(String json, Class<T> clazz) {
        return JSON.parseObject(json, clazz);
    }

    public static <T> T parseNotThrowException(String json, Class<T> clazz) {
        Object t = null;

        try {
            t = JSON.parseObject(json, clazz);
        } catch (Exception var4) {
        }

        return (T) t;
    }

    public static <T> List<T> parseArray(String json, Class<T> clazz) {
        return JSON.parseArray(json, clazz);
    }

    public static <T> List<T> parseArrayNotThrowException(String json, Class<T> clazz) {
        List list = null;

        try {
            list = JSON.parseArray(json, clazz);
        } catch (Exception var4) {
        }

        return list;
    }

    static {
        SimpleDateFormatSerializer df = new SimpleDateFormatSerializer("yyyy-MM-dd HH:mm:ss");
        config.put(Date.class, df);
        config.put(java.sql.Date.class, df);
        config.put(Timestamp.class, df);
        features = new SerializerFeature[]{SerializerFeature.WriteMapNullValue, SerializerFeature.WriteNullListAsEmpty, SerializerFeature.WriteNullNumberAsZero, SerializerFeature.WriteNullBooleanAsFalse, SerializerFeature.WriteNullStringAsEmpty, SerializerFeature.DisableCircularReferenceDetect};
    }
}
