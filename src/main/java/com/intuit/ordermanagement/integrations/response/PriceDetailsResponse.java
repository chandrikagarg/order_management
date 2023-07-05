package com.intuit.ordermanagement.integrations.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class PriceDetailsResponse extends  DownStreamServiceBaseResponse{

    @JsonProperty("data")
    private DataObject dataObject;

    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class DataObject {

        @JsonProperty("basePrice")
        private Double basePrice;

        @JsonProperty("taxes")
        private Double taxes;

        @JsonProperty("totalPrice")
        private Double totalPrice;
    }

}
