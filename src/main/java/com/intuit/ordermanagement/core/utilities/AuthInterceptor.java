//package com.intuit.ordermanagement.controller.utilities;
//
//import com.intuit.ordermanagement.service.interceptor.AuthInterceptorService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//import org.springframework.util.StringUtils;
//import org.springframework.web.servlet.ModelAndView;
//import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//@Component
//public class AuthInterceptor extends HandlerInterceptorAdapter {
//
//    @Autowired
//    private AuthInterceptorService;
//
//    @Override
//    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//        String token = request.getHeader("token");
//        if (StringUtils.hasText(token)) {
//            authInterceptorService.validateToken(token, request);
//            return true;
//        } else {
//            throw new Exception("Missing token");
//        }
//    }
//
//    @Override
//    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
//        super.postHandle(request, response, handler, modelAndView);
//    }
//
//}
