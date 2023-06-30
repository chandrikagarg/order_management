package com.intuit.ordermanagement.integrations.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.intuit.ordermanagement.core.dto.AdressDetails;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class PlaceOrderResponse extends  DownStreamServiceBaseResponse{

    @JsonProperty("data")
    private DataObject dataObject;

    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DataObject {

        @JsonProperty("orderId")
        private Double orderId;

        @JsonProperty
        private AdressDetails adressDetails;


    }

}
