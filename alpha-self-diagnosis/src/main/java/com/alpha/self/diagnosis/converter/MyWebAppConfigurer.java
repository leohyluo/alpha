package com.alpha.self.diagnosis.converter;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by xc.xiong on 2017/9/21.
 */
@Configuration
public class MyWebAppConfigurer extends WebMvcConfigurerAdapter {

    @Resource
    MyStringHttpMessageConverter myStringHttpMessageConverter;

    List<HttpMessageConverter<?>> converters;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(new CustomMethodArgumentResolver(converters));
        argumentResolvers.add(new RequestJsonParamMethodArgumentResolver());
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(myStringHttpMessageConverter);
        this.converters = converters;

    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(new RequestParamConvertInterceptor()).addPathPatterns("/**");
        super.addInterceptors(registry);
    }

}
