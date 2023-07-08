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
    public ResponseEntity<OrderMgmtAPIResponse> handleOrderNotCreatedExceptiom(OrderNotCreatedException ex) {
        log.error("OrderNotCreatedException {}", ex);
         MessageApiResponse messageApiResponse = MessageApiResponse.builder().text(ex.getErrorCode().getInstantDisplayMessage()).code(ex.getErrorCode().getCode()).build();
        return new ResponseEntity<>(OrderMgmtAPIResponse.buildFailure(messageApiResponse), HttpStatus.OK);
    }

    @ExceptionHandler(DownSTreamException.class)
    public ResponseEntity<OrderMgmtAPIResponse> handleDownSTreamException(DownSTreamException ex) {
        log.error("DownSTreamException {}", ex);
        MessageApiResponse messageApiResponse = MessageApiResponse.builder()
                .text(ex.getErrorCode().getInstantDisplayMessage())
                .code(ex.getErrorCode().getCode())
                .build();
        return new ResponseEntity<>(OrderMgmtAPIResponse.buildFailure(messageApiResponse), HttpStatus.OK);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<OrderMgmtAPIResponse> handleException(Exception ex) {
        log.error("Unhandled Exception", ex);
        MessageApiResponse messageApiResponse = MessageApiResponse.builder()
                .text("An unexpected error occurred.")
                .code("EOO")
                .build();
        return new ResponseEntity<>(OrderMgmtAPIResponse.buildFailure(messageApiResponse), HttpStatus.INTERNAL_SERVER_ERROR);
    }

}

