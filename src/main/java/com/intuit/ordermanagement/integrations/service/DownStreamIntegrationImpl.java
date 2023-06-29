package com.intuit.ordermanagement.integrations.service;

import com.intuit.ordermanagement.core.dto.AdressDetails;
import com.intuit.ordermanagement.core.entities.Product;
import com.intuit.ordermanagement.integrations.exceptions.DownSTreamException;
import com.intuit.ordermanagement.integrations.request.PriceDetailsRequest;
import com.intuit.ordermanagement.integrations.response.DownStreamServiceBaseResponse;
import com.intuit.ordermanagement.integrations.response.MessageApiResponse;
import com.intuit.ordermanagement.integrations.response.PriceDetailsResponse;
import com.intuit.ordermanagement.integrations.utils.DownStreamOperation;
import com.intuit.ordermanagement.integrations.utils.ErrorCode;
import com.intuit.ordermanagement.integrations.utils.JsonHelperImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.http.HttpHeaders;
import java.text.MessageFormat;
import java.util.List;
import java.util.concurrent.*;

@Slf4j
@Service
public class DownStreamIntegrationImpl implements IDownStreamIntegration{

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    private Executor executor;

    @Autowired
    JsonHelperImpl jsonHelper;

    @Override
    public PriceDetailsResponse getFinalProductDetails(PriceDetailsRequest priceDetailsRequest) throws Exception {
        HttpEntity httpEntity = new HttpEntity(priceDetailsRequest);

        String url = buildDownStreamServiceUrl(DownStreamOperation.GET_FINAL_PRICE_DETAILS);
        url = UriComponentsBuilder.fromHttpUrl(url).toUriString();

        PriceDetailsResponse priceDetailsResponse;

        try{
            String finalurl = url;
            CompletableFuture<ResponseEntity<String>> future = CompletableFuture.supplyAsync(()->
                    restTemplate.exchange(finalurl, HttpMethod.POST,httpEntity,String.class),executor);

            priceDetailsResponse = getDataResponse(future,PriceDetailsResponse.class,"DownStream Price Details API Response");
            return  priceDetailsResponse;

        }catch (HttpClientErrorException e){
            priceDetailsResponse = jsonHelper.fromJson(e.getResponseBodyAsString(), PriceDetailsResponse.class);
            log.error("Xtra ops Dashboard menu config API failure response : {}", jsonHelper.toJsonPretty(priceDetailsResponse), e);
            throw new DownSTreamException(new ErrorCode("DOWN_STR_01","Unable to hit downstream service"));

        }
    }

    private <V extends DownStreamServiceBaseResponse> V  getDataResponse(CompletableFuture<ResponseEntity<String>> future,
                                                 Class<V> responseType,
                                                 String logger) throws Exception {
        ResponseEntity<String> responseEntity;
        try{
            responseEntity = future.get(2000, TimeUnit.MILLISECONDS);
            log.info(logger + ": {}",responseEntity.getBody());
            DownStreamServiceBaseResponse baseResponse = jsonHelper.fromJson(responseEntity.getBody(), responseType);
            if(!baseResponse.getSuccess()){
                MessageApiResponse msg = baseResponse.getMsg();
                throw new DownSTreamException(new ErrorCode(msg.getCode(), msg.getText()));
            }
            return(V)baseResponse;
        }catch (InterruptedException | ExecutionException | TimeoutException ex){
            log.error("Exception occcured while fetching data from downstream service {} ",ex);
            throw  new Exception(ex.getMessage());
        }
    }

    private String buildDownStreamServiceUrl(DownStreamOperation downStreamOperation) {
        switch (downStreamOperation){

            case GET_FINAL_PRICE_DETAILS:
                return "get/final/price";
            default:
                throw new IllegalArgumentException("Invalid operation");

        }
    }
}
