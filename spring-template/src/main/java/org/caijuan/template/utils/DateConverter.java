package org.caijuan.template.utils;

import org.springframework.core.convert.converter.Converter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 实现 Converter 接口
 *
 * Converter<S,T>：将 S 类型对象转为 T 类型对象
 * ConverterFactory<S, R>：将 S 类型对象转为 R 类型及子类对象
 * GenericConverter：它支持多个source和目标类型的转化，同时还提供了source和目标类型的上下文，
 *                   这个上下文能让你实现基于属性上的注解或信息来进行类型转换。
 */
public class DateConverter implements Converter<String, Date> {

    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public Date convert(String source) {
        if (!"".equals(source)) {
            try {
                simpleDateFormat.parse(source);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}