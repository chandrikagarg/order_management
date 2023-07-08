package com.intuit.ordermanagement.service;

import com.intuit.ordermanagement.core.dao.OrderProductsMappingDao;
import com.intuit.ordermanagement.core.dao.OrderUserDetailsMappingDao;
import com.intuit.ordermanagement.core.dao.ProductsDao;
import com.intuit.ordermanagement.core.entities.OrderProductsMapping;
import com.intuit.ordermanagement.core.entities.OrderUserDetailsMapping;
import com.intuit.ordermanagement.core.entities.Product;
import com.intuit.ordermanagement.integrations.exceptions.DownSTreamException;
import com.intuit.ordermanagement.integrations.exceptions.OrderNotCreatedException;
import com.intuit.ordermanagement.integrations.request.OrderInitiationRequest;
import com.intuit.ordermanagement.integrations.request.PlaceOrderRequest;
import com.intuit.ordermanagement.integrations.response.DownStreamServiceBaseResponse;
import com.intuit.ordermanagement.integrations.response.MessageApiResponse;
import com.intuit.ordermanagement.integrations.response.PriceDetailsResponse;
import com.intuit.ordermanagement.integrations.response.TaxDetailsResponse;
import com.intuit.ordermanagement.integrations.service.IDownStreamIntegration;
import com.intuit.ordermanagement.integrations.utils.ErrorCode;
import com.intuit.ordermanagement.service.enums.CategoryEnum;
import com.intuit.ordermanagement.service.enums.OrderStatusEnum;
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

        PriceDetailsResponse priceDetailsResponse =  downStreamIntegration.getBasePriceForProduct(productId,userId,addressId);
        Double basePrice = priceDetailsResponse.getDataObject().getBasePrice();
        TaxDetailsResponse taxDetailsResponse = downStreamIntegration.getTaxDetailsForProduct(basePrice,productId,userId,addressId);
        Double tax = taxDetailsResponse.getDataObject().getTaxes();
        Map<String, Object> response = new HashMap<>();
        response.put("basePrice", basePrice);
        response.put("tax", tax);
        response.put("totalPrice", basePrice + tax);
        return OrderMgmtAPIResponse.buildSuccess(response);
    }

    @Override
    public OrderMgmtAPIResponse placeOrderForProducts(String userId, PlaceOrderRequest placeOrderRequest) throws Exception {
        String orderId = generateRandomId();
        String requestId = generateRandomId();
        try{
            OrderProductsMapping orderProductsMapping = OrderProductsMapping.builder().productId(placeOrderRequest.getProductId()).orderId(orderId).productsStatus(OrderStatusEnum.INITIATED).build();
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
            Map<String, Object> response = new HashMap<>();
            response.put("orderId", orderId);
            response.put("requestId",requestId);
            return OrderMgmtAPIResponse.buildSuccess(response);
        }
        MessageApiResponse messageApiResponse = MessageApiResponse.builder().text("Error in creating order Please try again after some time").code("UPS_02").build();
        return OrderMgmtAPIResponse.buildFailure(messageApiResponse);
    }

    private String generateRandomId() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }
}
