package com.intuit.ordermanagement.service.interceptor;

import com.intuit.ordermanagement.integrations.exceptions.AuthException;
import com.intuit.ordermanagement.integrations.exceptions.OrderNotCreatedException;
import com.intuit.ordermanagement.integrations.utils.ErrorCode;
import com.intuit.ordermanagement.service.enums.ErrorCodeEnum;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class CustomInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        // This method is called before the request handler is executed
        // Add your pre-processing logic here

        // Get the userId from the request header
        String userId = request.getHeader("userId");

        // Validate if userId is present
        if (userId == null || userId.isEmpty()) {
            throw new AuthException(new ErrorCode(ErrorCodeEnum.AUTH_01.name(), ErrorCodeEnum.AUTH_01.getMessage()));
        }

        // Add the userId to the request attributes for further processing
        request.setAttribute("userId", userId);

        return true; // Return true to allow the handler to proceed, or false to stop processing
    }

}