//package com.intuit.ordermanagement.controller.utilities.config;
//
//import com.intuit.ordermanagement.controller.utilities.AuthInterceptor;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.converter.HttpMessageConverter;
//import org.springframework.http.converter.StringHttpMessageConverter;
//import org.springframework.web.method.support.HandlerMethodArgumentResolver;
//import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//import org.springframework.web.servlet.mvc.method.annotation.RequestAttributeMethodArgumentResolver;
//
//import java.util.List;
//
//@Configuration
//public class MvcConfig implements WebMvcConfigurer {
//
//    @Autowired
//    AuthInterceptor authInterceptor;
//
//    @Override
//    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
//        argumentResolvers.add(new RequestAttributeMethodArgumentResolver());
//    }
//
//    @Override
//    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
//        converters.add(new StringHttpMessageConverter());
//        //converters.add(new MappingJackson2HttpMessageConverter(jsonHelper.getObjectMapper()));
//    }
//
//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(authInterceptor).addPathPatterns("/products/**").excludePathPatterns("p2p/ping");
//
//    }
//}