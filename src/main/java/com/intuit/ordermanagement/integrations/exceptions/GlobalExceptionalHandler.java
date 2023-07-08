package com.intuit.ordermanagement.integrations.exceptions;

import com.intuit.ordermanagement.integrations.response.MessageApiResponse;
import com.intuit.ordermanagement.service.response.OrderMgmtAPIResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
@Slf4j
public class GlobalExceptionalHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(OrderNotCreatedException.class)
    public ResponseEntity<OrderMgmtAPIResponse> handleUpgradeAccountException(OrderNotCreatedException ex) {
        log.error("OrderNotCreatedException {}", ex);
        Map<String, String> data = new HashMap<>();
        data.put("message", ex.getMessage());
         MessageApiResponse messageApiResponse = MessageApiResponse.builder().text(ex.getErrorCode().getInstantDisplayMessage()).code(ex.getErrorCode().getCode()).build();
        return new ResponseEntity<>(OrderMgmtAPIResponse.buildFailure(messageApiResponse), HttpStatus.OK);
    }
}

