package com.dihuan.common.config;


import com.dihuan.common.converter.BooleanToBooleanEnumConvertFactory;
import com.dihuan.common.converter.StringToBaseEnumConverterFactory;
import com.dihuan.common.interceptor.ModelTokenInfoInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {

    @Autowired
    private StringToBaseEnumConverterFactory stringToBaseEnumConverterFactory;

    @Autowired
    private BooleanToBooleanEnumConvertFactory booleanToBooleanEnumConvertFactory;

    @Autowired
    private ModelTokenInfoInterceptor modelTokenInfoInterceptor;


    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverterFactory(this.stringToBaseEnumConverterFactory);
        registry.addConverterFactory(this.booleanToBooleanEnumConvertFactory);
    }


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(this.modelTokenInfoInterceptor).addPathPatterns("/**");
    }
}
