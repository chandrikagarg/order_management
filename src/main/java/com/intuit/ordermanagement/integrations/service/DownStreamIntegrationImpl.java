package com.intuit.ordermanagement.integrations.service;

import com.intuit.ordermanagement.integrations.exceptions.DownSTreamException;
import com.intuit.ordermanagement.integrations.request.OrderInitiationRequest;
import com.intuit.ordermanagement.integrations.request.OrderSubmitRequest;
import com.intuit.ordermanagement.integrations.request.PlaceOrderRequest;
import com.intuit.ordermanagement.integrations.request.PriceDetailsRequest;
import com.intuit.ordermanagement.integrations.response.*;
import com.intuit.ordermanagement.integrations.utils.DownStreamOperation;
import com.intuit.ordermanagement.integrations.utils.ErrorCode;
import com.intuit.ordermanagement.integrations.utils.JsonHelperImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.concurrent.*;

@Slf4j
@Service
public class DownStreamIntegrationImpl implements IDownStreamIntegration {

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    private Executor executor;

    @Autowired
    JsonHelperImpl jsonHelper;

    @Override
    public PriceDetailsResponse getBasePriceForProduct(String productId, String userId) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.add("apiKey", "api-key");
        headers.add("apiPasscode", "api-passcode");
        headers.add("userId", userId);
        HttpEntity httpEntity = new HttpEntity(headers);

        String url = buildDownStreamServiceUrl(DownStreamOperation.GET_PRICE_DETAILS);
        url = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("productId", productId)
                .toUriString();

        PriceDetailsResponse priceDetailsResponse;

        try {
            String finalurl = url;
            CompletableFuture<ResponseEntity<String>> future = CompletableFuture.supplyAsync(() ->
                    restTemplate.exchange(finalurl, HttpMethod.GET, httpEntity, String.class), executor);

            priceDetailsResponse = getDataResponse(future, PriceDetailsResponse.class, "DownStream ProductCatalog Details API Response");
            return priceDetailsResponse;

        } catch (HttpClientErrorException e) {
            priceDetailsResponse = jsonHelper.fromJson(e.getResponseBodyAsString(), PriceDetailsResponse.class);
            log.error("Downstream service get products price failure response : {}", jsonHelper.toJsonPretty(priceDetailsResponse), e);
            throw new DownSTreamException(new ErrorCode("DOWN_STR_01", "Unable to hit ProductCatalog downstream service"));

        }
    }

    @Override
    public TaxDetailsResponse getTaxDetailsForProduct(Double price, String productId, String userId, String addressId) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.add("apiKey", "api-key");
        headers.add("apiPasscode", "api-passcode");
        headers.add("userId", userId);
        HttpEntity httpEntity = new HttpEntity(headers);

        String url = buildDownStreamServiceUrl(DownStreamOperation.GET_TAX_DETAILS);
        url = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("addressId", addressId)
                .queryParam("productId", productId)
                .queryParam("price", price)
                .toUriString();

        TaxDetailsResponse priceDetailsResponse;

        try {
            String finalurl = url;
            CompletableFuture<ResponseEntity<String>> future = CompletableFuture.supplyAsync(() ->
                    restTemplate.exchange(finalurl, HttpMethod.GET, httpEntity, String.class), executor);

            priceDetailsResponse = getDataResponse(future, TaxDetailsResponse.class, "DownStream Tax details  API Response");
            return priceDetailsResponse;

        } catch (HttpClientErrorException e) {
            priceDetailsResponse = jsonHelper.fromJson(e.getResponseBodyAsString(), TaxDetailsResponse.class);
            log.error("Downstream service Tax details   failure response : {}", jsonHelper.toJsonPretty(priceDetailsResponse), e);
            throw new DownSTreamException(new ErrorCode("DOWN_STR_01", "Unable to hit Tax details downstream service"));

        }
    }

    @Override
    public DownStreamServiceBaseResponse submitOrderForBilling(OrderSubmitRequest placeOrderRequest, String userId) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.add("apiKey", "api-key");
        headers.add("apiPasscode", "api-passcode");
        headers.add("userId", userId);
        HttpEntity<OrderSubmitRequest> httpEntity = new HttpEntity<>(placeOrderRequest, headers);

        String url = buildDownStreamServiceUrl(DownStreamOperation.PLACE_ORDER);
        url = UriComponentsBuilder.fromHttpUrl(url)
                .toUriString();

        DownStreamServiceBaseResponse priceDetailsResponse;

        try {
            String finalurl = url;
            CompletableFuture<ResponseEntity<String>> future = CompletableFuture.supplyAsync(() ->
                    restTemplate.exchange(finalurl, HttpMethod.POST, httpEntity, String.class), executor);

            priceDetailsResponse = getDataResponse(future, DownStreamServiceBaseResponse.class, "DownStream Tax details  API Response");
            return priceDetailsResponse;

        } catch (HttpClientErrorException e) {
            priceDetailsResponse = jsonHelper.fromJson(e.getResponseBodyAsString(), DownStreamServiceBaseResponse.class);
            log.error("Downstream service Tax details   failure response : {}", jsonHelper.toJsonPretty(priceDetailsResponse), e);
            throw new DownSTreamException(new ErrorCode("DOWN_STR_01", "Unable to hit Tax details downstream service"));

        }
    }

    public DownStreamServiceBaseResponse informOrderInitiation(String userId, OrderInitiationRequest placeOrderRequest) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.add("apiKey", "api-key");
        headers.add("apiPasscode", "api-passcode");
        headers.add("userId", userId);
        HttpEntity<OrderInitiationRequest> httpEntity = new HttpEntity<>(placeOrderRequest, headers);

        String url = buildDownStreamServiceUrl(DownStreamOperation.ORDER_INITIATE_PG_SERVICE);
        url = UriComponentsBuilder.fromHttpUrl(url)
                .toUriString();

        DownStreamServiceBaseResponse priceDetailsResponse;

        try {
            String finalurl = url;
            CompletableFuture<ResponseEntity<String>> future = CompletableFuture.supplyAsync(() ->
                    restTemplate.exchange(finalurl, HttpMethod.POST, httpEntity, String.class), executor);

            priceDetailsResponse = getDataResponse(future, DownStreamServiceBaseResponse.class, "DownStream  detailsOrder initiation request  API Response");
            return priceDetailsResponse;

        } catch (HttpClientErrorException e) {
            priceDetailsResponse = jsonHelper.fromJson(e.getResponseBodyAsString(), DownStreamServiceBaseResponse.class);
            log.error("Order Initiation payment service failure response : {}", jsonHelper.toJsonPretty(priceDetailsResponse), e);
            throw new DownSTreamException(new ErrorCode("DOWN_STR_01", "Unable to hit Order Inititaion Request Payment service"));

        }
    }

    @Override
    public DownStreamServiceBaseResponse sendEmail(PlaceOrderRequest placeOrderRequest, String userId) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.add("apiKey", "api-key");
        headers.add("apiPasscode", "api-passcode");
        headers.add("userId", userId);

        HttpEntity<PlaceOrderRequest> httpEntity = new HttpEntity<>(placeOrderRequest, headers);

        String url = buildDownStreamServiceUrl(DownStreamOperation.SEND_EMAIL);
        url = UriComponentsBuilder.fromHttpUrl(url)
                .toUriString();

        DownStreamServiceBaseResponse priceDetailsResponse;

        try {
            String finalurl = url;
            CompletableFuture<ResponseEntity<String>> future = CompletableFuture.supplyAsync(() ->
                    restTemplate.exchange(finalurl, HttpMethod.POST, httpEntity, String.class), executor);

            priceDetailsResponse = getDataResponse(future, DownStreamServiceBaseResponse.class, "DownStream Tax details  API Response");
            return priceDetailsResponse;

        } catch (HttpClientErrorException e) {
            priceDetailsResponse = jsonHelper.fromJson(e.getResponseBodyAsString(), DownStreamServiceBaseResponse.class);
            log.error("Downstream service Tax details   failure response : {}", jsonHelper.toJsonPretty(priceDetailsResponse), e);
            throw new DownSTreamException(new ErrorCode("DOWN_STR_01", "Unable to hit Tax details downstream service"));

        }
    }

    private <V extends DownStreamServiceBaseResponse> V getDataResponse(CompletableFuture<ResponseEntity<String>> future,
                                                                        Class<V> responseType,
                                                                        String logger) throws Exception {
        ResponseEntity<String> responseEntity;
        try {
            responseEntity = future.get(2000, TimeUnit.MILLISECONDS);
            log.info(logger + ": {}", responseEntity.getBody());
            DownStreamServiceBaseResponse baseResponse = jsonHelper.fromJson(responseEntity.getBody(), responseType);
            if (!baseResponse.getSuccess()) {
                MessageApiResponse msg = baseResponse.getMsg();
                throw new DownSTreamException(new ErrorCode(msg.getCode(), msg.getText()));
            }
            return (V) baseResponse;
        } catch (InterruptedException | ExecutionException | TimeoutException ex) {
            log.error("Exception occcured while fetching data from downstream service {} ", ex);
            throw new Exception(ex.getMessage());
        }
    }

    private String buildDownStreamServiceUrl(DownStreamOperation downStreamOperation) {
        switch (downStreamOperation) {

            case GET_PRICE_DETAILS:
                return "http://localhost:8081/dns/product/price";
            case GET_TAX_DETAILS:
                return "http://localhost:8081/dns/product/tax";
            case ORDER_INITIATE_PG_SERVICE:
                return "http://localhost:8081/dns/order/initiate";
            case PLACE_ORDER:
                return "http://localhost:8081/dns/submit/order";
            case SEND_EMAIL:
                return "https://email-service/dns/email";
            default:
                throw new IllegalArgumentException("Invalid operation");

        }
    }
}
