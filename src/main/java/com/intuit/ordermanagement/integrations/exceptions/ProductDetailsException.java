package com.intuit.ordermanagement.integrations.exceptions;

import com.intuit.ordermanagement.integrations.utils.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ProductDetailsException extends RuntimeException {

    private String message;

    private ErrorCode errorCode;

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public ProductDetailsException(String message) {
        super(message);
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public ProductDetailsException(ErrorCode errorCode) {
        super(errorCode.getInstantDisplayMessage());
        this.errorCode = errorCode;
        this.message = errorCode.getInstantDisplayMessage();
    }
}