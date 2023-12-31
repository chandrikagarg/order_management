package com.intuit.ordermanagement.integrations.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class TaxDetailsResponse extends  DownStreamServiceBaseResponse{

    @JsonProperty("data")
    private DataObject dataObject;

    @Setter
    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class DataObject {

        @JsonProperty("basePrice")
        private Double basePrice;

        @JsonProperty("taxes")
        private Double taxes;
    }

}
