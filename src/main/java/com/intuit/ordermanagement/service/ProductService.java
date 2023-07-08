package com.intuit.ordermanagement.service;

import com.intuit.ordermanagement.core.dao.OrderProductsMappingDao;
import com.intuit.ordermanagement.core.dao.OrderUserDetailsMappingDao;
import com.intuit.ordermanagement.core.dao.ProductsDao;
import com.intuit.ordermanagement.core.entities.OrderProductsMapping;
import com.intuit.ordermanagement.core.entities.OrderUserDetailsMapping;
import com.intuit.ordermanagement.core.entities.Product;
import com.intuit.ordermanagement.integrations.exceptions.DownSTreamException;
import com.intuit.ordermanagement.integrations.exceptions.OrderNotCreatedException;
import com.intuit.ordermanagement.integrations.request.CallbackPaymentRequest;
import com.intuit.ordermanagement.integrations.request.OrderInitiationRequest;
import com.intuit.ordermanagement.integrations.request.OrderSubmitRequest;
import com.intuit.ordermanagement.integrations.request.PlaceOrderRequest;
import com.intuit.ordermanagement.integrations.response.DownStreamServiceBaseResponse;
import com.intuit.ordermanagement.integrations.response.MessageApiResponse;
import com.intuit.ordermanagement.integrations.response.PriceDetailsResponse;
import com.intuit.ordermanagement.integrations.response.TaxDetailsResponse;
import com.intuit.ordermanagement.integrations.service.IDownStreamIntegration;
import com.intuit.ordermanagement.integrations.utils.ErrorCode;
import com.intuit.ordermanagement.service.enums.CategoryEnum;
import com.intuit.ordermanagement.service.enums.OrderStatusEnum;
import com.intuit.ordermanagement.service.enums.PaymentStatusEnum;
import com.intuit.ordermanagement.service.response.OrderMgmtAPIResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
public class ProductService implements IProductService{

    @Autowired
    ProductsDao productsDao;

    @Autowired
    IDownStreamIntegration downStreamIntegration;

    @Autowired
    OrderUserDetailsMappingDao orderUserDetailsMappingDao;

    @Autowired
    OrderProductsMappingDao orderProductsMappingDao;

    @Override
    public List<Product> findByCategory(CategoryEnum category) {
       return  productsDao.findByCategory(category);
    }

    @Override
    public OrderMgmtAPIResponse findFinalPriceForProducts(String productId, String userId, String addressId) throws Exception {

        PriceDetailsResponse priceDetailsResponse =  downStreamIntegration.getBasePriceForProduct(productId,userId);
        Double basePrice = priceDetailsResponse.getDataObject().getBasePrice();
        TaxDetailsResponse taxDetailsResponse = downStreamIntegration.getTaxDetailsForProduct(basePrice,productId,userId,addressId);
        Double tax = taxDetailsResponse.getDataObject().getTaxes();
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
        //TODO amount verify received from front end and calculate the final price on the basis of productId received
        try{
            orderProductsMapping = OrderProductsMapping.builder().productId(placeOrderRequest.getProductId()).orderId(orderId).productsStatus(OrderStatusEnum.INITIATED).build();
            OrderUserDetailsMapping orderUserDetailsMapping = OrderUserDetailsMapping.builder().orderId(orderId).requestId(requestId).amount(placeOrderRequest.getAmount()).addressId(placeOrderRequest.getAddressId()).build();
            orderUserDetailsMappingDao.save(orderUserDetailsMapping);
            orderProductsMappingDao.save(orderProductsMapping);
        }catch (Exception e){
            log.error("Error in creating the order {}",e.getMessage(),e);
            throw new OrderNotCreatedException(new ErrorCode("UPS_01", "Unable to create order"));
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
            Map<String, Object> response = new HashMap<>();
            response.put("orderId", orderId);
            response.put("requestId",requestId);
            return OrderMgmtAPIResponse.buildSuccess(response);
        }
        MessageApiResponse messageApiResponse = MessageApiResponse.builder().text("Error in creating order Please try again after some time").code("UPS_02").build();
        return OrderMgmtAPIResponse.buildFailure(messageApiResponse);
    }

    @Override
    public OrderMgmtAPIResponse getOrderStatus(String userId, String orderId) {
        OrderProductsMapping orderProductsMapping = orderProductsMappingDao.findByOrderId(orderId);
        OrderStatusEnum orderStatusEnum = orderProductsMapping.getProductsStatus();
        String message = getOrderStatusMessage(orderStatusEnum);
        //TODO return proper order details response through page render
        Map<String, Object> response = new HashMap<>();
        response.put("status", orderStatusEnum.name());
        response.put("message", message);
        response.put("details","details");
        return OrderMgmtAPIResponse.buildSuccess(response);

    }

    @Override
    public OrderMgmtAPIResponse updatePaymentStatus(String userId, CallbackPaymentRequest callbackPaymentRequest) throws Exception {
        String paymentStatus = callbackPaymentRequest.getPaymentStatus();
        if(paymentStatus.equals(PaymentStatusEnum.SUCCESS.name())){
            String requestId = callbackPaymentRequest.getRequestId();
            OrderUserDetailsMapping orderUserDetailsMapping = orderUserDetailsMappingDao.findByRequestId(requestId);
            OrderProductsMapping orderProductsMapping = orderProductsMappingDao.findByOrderId(orderUserDetailsMapping.getOrderId());
            OrderSubmitRequest orderSubmitRequest = OrderSubmitRequest.builder()
                    .productId(orderProductsMapping.getProductId())
                    .addressId(orderUserDetailsMapping.getAddressId())
                    .orderId(orderUserDetailsMapping.getOrderId())
                    .amount(orderUserDetailsMapping.getAmount()).build();

            downStreamIntegration.submitOrderForBilling(orderSubmitRequest, userId);
            publishEventforSendingEmail(userId);

        }else if(paymentStatus.equals(PaymentStatusEnum.FAILED.name())){
            publishEventforSendingEmail(userId);
        }
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Callback received successfullt");
        return OrderMgmtAPIResponse.buildSuccess(response);

    }

    private void publishEventforSendingEmail(String userId) {
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
