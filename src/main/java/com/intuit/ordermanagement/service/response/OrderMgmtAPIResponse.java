package com.intuit.ordermanagement.service.response;

import com.intuit.ordermanagement.integrations.response.MessageApiResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderMgmtAPIResponse {

    private Object data;

    private Boolean success;

    private MessageApiResponse message;


    public OrderMgmtAPIResponse(Object data, boolean success, MessageApiResponse message) {
        this.data = data;
        this.success = success;
        this.message = message;
    }

    public static OrderMgmtAPIResponse buildFailure(MessageApiResponse message) {
        if (message == null)
            throw new IllegalArgumentException("Invalid arguments");

        Map<String, String> data = new HashMap<>();
        data.put("message", message.getText());

        OrderMgmtAPIResponse orderMgmtAPIResponse = new OrderMgmtAPIResponse(data, false, message);

        log.info("OrderMgmtAPIResponse: " + orderMgmtAPIResponse);
        return orderMgmtAPIResponse;
    }

    public static OrderMgmtAPIResponse buildFailure(MessageApiResponse message, Object data) {
        if (message == null)
            throw new IllegalArgumentException("Invalid arguments");

        OrderMgmtAPIResponse orderMgmtAPIResponse = new OrderMgmtAPIResponse(data, false, message);

        log.info("OrderMgmtAPIResponse: " + orderMgmtAPIResponse);
        return orderMgmtAPIResponse;
    }

    public static OrderMgmtAPIResponse buildSuccess(Object data) {

        OrderMgmtAPIResponse orderMgmtAPIResponse = new OrderMgmtAPIResponse(data, true,null);
        log.info("OrderMgmtAPIResponse: " + orderMgmtAPIResponse);
        return orderMgmtAPIResponse;
    }
    public static OrderMgmtAPIResponse buildSuccess(MessageApiResponse message, Object data) {
        if (message == null)
            throw new IllegalArgumentException("Invalid arguments");

        OrderMgmtAPIResponse orderMgmtAPIResponse = new OrderMgmtAPIResponse(data, true, message);

        log.info("OrderMgmtAPIResponse: " + orderMgmtAPIResponse);
        return orderMgmtAPIResponse;
    }


}