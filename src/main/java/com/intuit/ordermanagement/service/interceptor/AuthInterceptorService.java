//package com.intuit.ordermanagement.service.interceptor;
//
//import com.intuit.ordermanagement.core.dao.AuthDAO;
//import com.intuit.ordermanagement.core.entities.AuthEntity;
//import jakarta.security.auth.message.AuthException;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.util.StringUtils;
//
//import javax.servlet.http.HttpServletRequest;
//
//@Slf4j
//@Service
//public class AuthInterceptorService {
//
//    @Autowired
//    AuthDAO authDAO;
//
//    public AuthEntity validateToken(String token, HttpServletRequest request) throws AuthException {
//        String[] tokenParts;
//        String tokenId;
//        String hashId;
//
//        try {
//            tokenParts = token.split("\\.", 2);
//            hashId = tokenParts[0];
//            tokenId = tokenParts[1];
//
//            AuthEntity authEntity = authDAO.get(tokenId);
//
//            if (StringUtils.isEmpty(hashId) || StringUtils.isEmpty(tokenId)) {
//                throw new AuthException("Missing auth");
//            }
//            if (!authEntity.getHashId().equalsIgnoreCase(hashId))
//                throw new AuthException("Valid token id, but invalid hash id");
//
//            return authEntity;
//
//        } catch (Exception e) {
//            throw new AuthException();
//        }
//
//    }
//}
