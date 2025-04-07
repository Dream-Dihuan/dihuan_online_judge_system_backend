package com.dihuan.common.converter;

import com.dihuan.model.enums.BooleanEnum;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;
import org.springframework.stereotype.Component;

@Component
public class BooleanToBooleanEnumConvertFactory implements ConverterFactory<String, BooleanEnum> {
    @Override
    public <T extends BooleanEnum> Converter<String, T> getConverter(Class<T> targetType) {
        return new Converter<String, T>() {
            @Override
            public T convert(String source) {
                for (T enumConstant : targetType.getEnumConstants()) {
                      if(enumConstant.getStatus().equals(Boolean.valueOf(source))){
                          return enumConstant;
                      }
                  }
                  throw new IllegalArgumentException("非法的枚举值："+source);
            }
        };
    }
}

/**
 * package site.dihuan.platform.web.admin.custom.converter;
 *
 * import org.springframework.core.convert.converter.Converter;
 * import org.springframework.core.convert.converter.ConverterFactory;
 * import org.springframework.stereotype.Component;
 * import site.dihuan.platform.enums.BaseEnum;
 *
 * @Component
 * public class StringToBaseEnumConverterFactory implements ConverterFactory<String, BaseEnum> {
 *     @Override
 *     public <T extends BaseEnum> Converter<String, T> getConverter(Class<T> targetType) {
 *         return new Converter<String, T>() {
 *             @Override
 *             public T convert(String source) {
 *                 for (T enumConstant : targetType.getEnumConstants()) {
 *                     if(enumConstant.getCode().equals(Integer.valueOf(source))){
 *                         return enumConstant;
 *                     }
 *                 }
 *                 throw new IllegalArgumentException("非法的枚举值："+source);
 *             }
 *         };
 *     }
 * }
 */