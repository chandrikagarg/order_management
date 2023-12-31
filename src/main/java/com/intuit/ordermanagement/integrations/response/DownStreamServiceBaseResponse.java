package com.intuit.ordermanagement.integrations.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class DownStreamServiceBaseResponse {

    @JsonProperty
    private Object data;

    @JsonProperty("success")
    private  Boolean success;

    @JsonProperty("message")
    private MessageApiResponse msg;
}
