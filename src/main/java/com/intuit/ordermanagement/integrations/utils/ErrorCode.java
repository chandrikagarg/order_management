package com.intuit.ordermanagement.integrations.utils;

import lombok.Getter;

@Getter
public class ErrorCode {
    private String instantDisplayMessage;
    private String code;

    public ErrorCode(String code, String instantDisplayMessage) {
        this.code = code;
        this.instantDisplayMessage = instantDisplayMessage;
    }

    public String getInstantDisplayMessage() {
        return instantDisplayMessage;
    }

    public String getCode() {
        return code;
    }

}