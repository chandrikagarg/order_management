package com.intuit.ordermanagement.service;

import com.intuit.ordermanagement.core.dao.OrderProductsMappingDao;
import com.intuit.ordermanagement.core.dao.OrderUserDetailsMappingDao;
import com.intuit.ordermanagement.core.dao.ProductsDao;
import com.intuit.ordermanagement.core.entities.OrderProductsMapping;
import com.intuit.ordermanagement.core.entities.OrderUserDetailsMapping;
import com.intuit.ordermanagement.integrations.exceptions.OrderNotCreatedException;
import com.intuit.ordermanagement.integrations.exceptions.OrderStatusException;
import com.intuit.ordermanagement.integrations.exceptions.ProductDetailsException;
import com.intuit.ordermanagement.integrations.request.CallbackPaymentRequest;
import com.intuit.ordermanagement.integrations.request.OrderInitiationRequest;
import com.intuit.ordermanagement.integrations.request.OrderSubmitRequest;
import com.intuit.ordermanagement.integrations.request.PlaceOrderRequest;
import com.intuit.ordermanagement.integrations.response.DownStreamServiceBaseResponse;
import com.intuit.ordermanagement.integrations.response.PriceDetailsResponse;
import com.intuit.ordermanagement.integrations.response.TaxDetailsResponse;
import com.intuit.ordermanagement.integrations.service.IDownStreamIntegration;
import com.intuit.ordermanagement.integrations.utils.ErrorCode;
import com.intuit.ordermanagement.service.enums.ErrorCodeEnum;
import com.intuit.ordermanagement.service.enums.OrderStatusEnum;
import com.intuit.ordermanagement.service.enums.PaymentStatusEnum;
import com.intuit.ordermanagement.service.response.OrderMgmtAPIResponse;
import com.mysql.cj.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
public class ProductService implements IUpstreamService {

    @Autowired
    ProductsDao productsDao;

    @Autowired
    IDownStreamIntegration downStreamIntegration;

    @Autowired
    OrderUserDetailsMappingDao orderUserDetailsMappingDao;

    @Autowired
    OrderProductsMappingDao orderProductsMappingDao;

//    @Override
//    public List<Product> findByCategory(CategoryEnum category) {
//       return  productsDao.findByCategory(category);
//    }

    @Override
    public OrderMgmtAPIResponse findFinalPriceForProducts(String productId, String userId, String addressId) throws Exception {
        log.info("Request received for the productId {} userId{} and addressId {}",productId,userId,addressId);
        if(StringUtils.isNullOrEmpty(productId) || StringUtils.isNullOrEmpty(addressId)){
            throw new ProductDetailsException(new ErrorCode(ErrorCodeEnum.P_01.name(), ErrorCodeEnum.P_01.getMessage()));
        }
        PriceDetailsResponse priceDetailsResponse =  downStreamIntegration.getBasePriceForProduct(productId,userId);
        Double basePrice = priceDetailsResponse.getDataObject().getBasePrice();
        if(basePrice == null || basePrice <=0){
            throw new ProductDetailsException(new ErrorCode(ErrorCodeEnum.P_02.name(), ErrorCodeEnum.P_02.getMessage()));
        }
        TaxDetailsResponse taxDetailsResponse = downStreamIntegration.getTaxDetailsForProduct(basePrice,productId,userId,addressId);
        Double tax = taxDetailsResponse.getDataObject().getTaxes();
        if(tax == null || tax <=0){
            throw new ProductDetailsException(new ErrorCode(ErrorCodeEnum.P_03.name(), ErrorCodeEnum.P_03.getMessage()));
        }
        Map<String, Object> response = new HashMap<>();
        //TODO page render framework for returning the response to front end
        response.put("basePrice", basePrice);
        response.put("tax", tax);
        response.put("totalPrice", basePrice + tax);
        return OrderMgmtAPIResponse.buildSuccess(response);
    }

    @Override
    public OrderMgmtAPIResponse placeOrderForProducts(String userId, PlaceOrderRequest placeOrderRequest) throws Exception {
        String orderId = generateRandomId();
        String requestId = generateRandomId();
        OrderProductsMapping orderProductsMapping;
        if(placeOrderRequest == null){
            throw  new OrderNotCreatedException(new ErrorCode(ErrorCodeEnum.O_06.name(),ErrorCodeEnum.O_06.getMessage()));
        }
        //TODO amount verify received from front end and calculate the final price on the basis of productId received
        try{
            orderProductsMapping = OrderProductsMapping.builder().productId(placeOrderRequest.getProductId()).orderId(orderId).productsStatus(OrderStatusEnum.INITIATED).build();
            OrderUserDetailsMapping orderUserDetailsMapping = OrderUserDetailsMapping.builder()
                    .orderId(orderId)
                    .requestId(requestId)
                    .userId(userId)
                    .amount(placeOrderRequest.getAmount())
                    .addressId(placeOrderRequest.getAddressId()).build();
            orderUserDetailsMappingDao.save(orderUserDetailsMapping);
            log.info("saved the order user mapping details for order id {}",orderId);
            orderProductsMappingDao.save(orderProductsMapping);
            log.info("saved the order product mapping details for order id {}",orderId);
        }catch (Exception e){
            log.error("Error in creating the order {}",e.getMessage(),e);
            throw new OrderNotCreatedException(new ErrorCode(ErrorCodeEnum.O_01.name(), ErrorCodeEnum.O_01.getMessage()));
        }
        OrderInitiationRequest orderInitiationRequest = OrderInitiationRequest.builder()
                .requestId(requestId)
                .orderId(orderId)
                .addressId(placeOrderRequest.getAddressId())
                .productId(placeOrderRequest.getProductId())
                .amount(placeOrderRequest.getAmount()).build();

        DownStreamServiceBaseResponse downStreamServiceBaseResponse = downStreamIntegration.informOrderInitiation(userId,orderInitiationRequest);
        if(downStreamServiceBaseResponse.getSuccess()){
            orderProductsMapping.setProductsStatus(OrderStatusEnum.PAYMENT_REQ_ACK);
            orderProductsMappingDao.save(orderProductsMapping);
            log.info("updated the status to payment request ack for order id {}",orderId);
            Map<String, Object> response = new HashMap<>();
            response.put("orderId", orderId);
            response.put("requestId",requestId);
            return OrderMgmtAPIResponse.buildSuccess(response);
        }
        throw new OrderNotCreatedException(new ErrorCode(ErrorCodeEnum.O_01.name(), ErrorCodeEnum.O_01.getMessage()));
    }

    @Override
    public OrderMgmtAPIResponse getOrderStatus(String userId, String orderId) {
        OrderProductsMapping orderProductsMapping = orderProductsMappingDao.findByOrderId(orderId);
        if(orderProductsMapping == null){
            throw new OrderNotCreatedException(new ErrorCode(ErrorCodeEnum.O_02.name(), ErrorCodeEnum.O_02.getMessage()));
        }
        OrderStatusEnum orderStatusEnum = orderProductsMapping.getProductsStatus();
        String message = getOrderStatusMessage(orderStatusEnum);
        log.info("order status api message{} for order id {}",message,orderId);
        //TODO return proper order details response through page render
        Map<String, Object> response = new HashMap<>();
        response.put("status", orderStatusEnum.name());
        response.put("message", message);
        response.put("details","details");
        return OrderMgmtAPIResponse.buildSuccess(response);

    }

    @Override
    public OrderMgmtAPIResponse updatePaymentStatus(String userId, CallbackPaymentRequest callbackPaymentRequest) throws Exception {
        if(callbackPaymentRequest == null){
            throw new OrderStatusException(new ErrorCode(ErrorCodeEnum.O_05.name(),ErrorCodeEnum.O_05.getMessage()));
        }
        String paymentStatus = callbackPaymentRequest.getPaymentStatus();
        String requestId = callbackPaymentRequest.getRequestId();
        //TODO terminal state status should not be updated again
        log.info("Callback request received for user {} for requestid {}",userId,requestId);
        OrderUserDetailsMapping orderUserDetailsMapping = orderUserDetailsMappingDao.findByRequestId(requestId);
        if(orderUserDetailsMapping == null){
            throw new OrderStatusException(new ErrorCode(ErrorCodeEnum.O_03.name(), ErrorCodeEnum.O_03.getMessage()));
        }
        OrderProductsMapping orderProductsMapping = orderProductsMappingDao.findByOrderId(orderUserDetailsMapping.getOrderId());
        if(paymentStatus.equals(PaymentStatusEnum.SUCCESS.name())){
            OrderSubmitRequest orderSubmitRequest = OrderSubmitRequest.builder()
                    .productId(orderProductsMapping.getProductId())
                    .addressId(orderUserDetailsMapping.getAddressId())
                    .orderId(orderUserDetailsMapping.getOrderId())
                    .amount(orderUserDetailsMapping.getAmount()).build();
            orderProductsMapping.setProductsStatus(OrderStatusEnum.PAYMENT_SUCCESS);
            try{
                orderProductsMappingDao.save(orderProductsMapping);
                log.info("Updated the order payment status to success for user {} for requestid {}",userId,requestId);
            }catch (Exception e){
                throw new OrderStatusException(new ErrorCode(ErrorCodeEnum.O_04.name(), ErrorCodeEnum.O_04.getMessage()));
            }

            downStreamIntegration.submitOrderForBilling(orderSubmitRequest, userId);
            log.info("Processed the order for billing for user {} and order Id {}",userId,orderUserDetailsMapping.getOrderId());

            publishEventforSendingEmail(userId);
            log.info("published the event for sending email for the for user {} and order Id {}",userId,orderUserDetailsMapping.getOrderId());

        }else if(paymentStatus.equals(PaymentStatusEnum.FAILED.name())){
            orderProductsMapping.setProductsStatus(OrderStatusEnum.PAYMENT_FAILED);
            orderProductsMappingDao.save(orderProductsMapping);
            log.info("Updated the order payment status to failed for user {} for requestid {}",userId,requestId);
            publishEventforSendingEmail(userId);
            log.info("published the event for sending email for the for user {} and order Id {}",userId,orderUserDetailsMapping.getOrderId());
        }
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Callback received successfullt");
        return OrderMgmtAPIResponse.buildSuccess(response);
    }

    private void publishEventforSendingEmail(String userId) {
        //TODO use JMS queue for sending
        //TODO send userDetails,addressdetails,paymentDetails,productId,orderId
    }

    /**
     * on the basis of status return the message to the app
     * @param orderStatusEnum
     * @return
     */
    private String getOrderStatusMessage(OrderStatusEnum orderStatusEnum) {
        return "Order placed successfully";
    }

    private String generateRandomId() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }
}
