package com.intuit.ordermanagement.test;

import com.intuit.ordermanagement.core.dao.OrderProductsMappingDao;
import com.intuit.ordermanagement.core.dao.OrderUserDetailsMappingDao;
import com.intuit.ordermanagement.core.dao.ProductsDao;
import com.intuit.ordermanagement.core.entities.OrderProductsMapping;
import com.intuit.ordermanagement.integrations.request.OrderInitiationRequest;
import com.intuit.ordermanagement.integrations.request.PlaceOrderRequest;
import com.intuit.ordermanagement.integrations.response.DownStreamServiceBaseResponse;
import com.intuit.ordermanagement.integrations.response.PriceDetailsResponse;
import com.intuit.ordermanagement.integrations.response.TaxDetailsResponse;
import com.intuit.ordermanagement.integrations.service.IDownStreamIntegration;
import com.intuit.ordermanagement.service.ProductService;
import com.intuit.ordermanagement.service.enums.OrderStatusEnum;
import com.intuit.ordermanagement.service.response.OrderMgmtAPIResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

public class ProductServiceTest {


    @Mock
    private IDownStreamIntegration downStreamIntegration;

    @Mock
    private OrderProductsMappingDao orderProductsMappingDao;

    @InjectMocks
    private ProductService productService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void testPriceDetailsResponse() {
        // Create test data
        Double basePrice = 10.0;

        // Create the response object
        PriceDetailsResponse response = new PriceDetailsResponse();
        PriceDetailsResponse.DataObject dataObject = new PriceDetailsResponse.DataObject();
        dataObject.setBasePrice(basePrice);
        response.setDataObject(dataObject);

        // Test the response object
        assertEquals(basePrice, response.getDataObject().getBasePrice());
    }


    @Test
    void findFinalPriceForProducts_Success() throws Exception {
        // Mocking the downstream integration service
        // Create the response object
        Double basePrice = 10.0;
        PriceDetailsResponse priceDetailsResponse = new PriceDetailsResponse();
        PriceDetailsResponse.DataObject dataObject = new PriceDetailsResponse.DataObject();
        dataObject.setBasePrice(basePrice);
        priceDetailsResponse.setDataObject(dataObject);

        TaxDetailsResponse taxDetailsResponse = new TaxDetailsResponse();
        TaxDetailsResponse.DataObject taxDetails = new TaxDetailsResponse.DataObject();
        taxDetails.setTaxes(2.0);
        taxDetailsResponse.setDataObject(taxDetails);

        when(downStreamIntegration.getBasePriceForProduct(anyString(), anyString()))
                .thenReturn(priceDetailsResponse);
        when(downStreamIntegration.getTaxDetailsForProduct(anyDouble(), anyString(), anyString(), anyString()))
                .thenReturn(taxDetailsResponse);

        // Test parameters
        String productId = "P123";
        String userId = "U123";
        String addressId = "A123";

        // Perform the test
        OrderMgmtAPIResponse response = productService.findFinalPriceForProducts(productId, userId, addressId);

        // Assertions
        assertEquals(true, response.getSuccess());
        assertEquals(12.0, ((Map<String, Object>) response.getData()).get("totalPrice"));
    }


    @Test
    void findFinalPriceForProducts_Exception() throws Exception {
        // Mocking the downstream integration service
        when(downStreamIntegration.getBasePriceForProduct(anyString(), anyString()))
                .thenThrow(new Exception("Some error occurred"));

        // Test parameters
        String productId = "P123";
        String userId = "U123";
        String addressId = "A123";

        // Perform the test and assert the exception
        assertThrows(Exception.class,
                () -> productService.findFinalPriceForProducts(productId, userId, addressId));
    }

    @Test
    void placeOrderForProducts_Success() throws Exception {
        // Mocking the downstream integration service
        DownStreamServiceBaseResponse downStreamServiceBaseResponse = new DownStreamServiceBaseResponse();
        downStreamServiceBaseResponse.setSuccess(true);

        when(downStreamIntegration.informOrderInitiation(anyString(), any(OrderInitiationRequest.class)))
                .thenReturn(downStreamServiceBaseResponse);

        // Test parameters
        String userId = "U123";
        PlaceOrderRequest placeOrderRequest = new PlaceOrderRequest();
        placeOrderRequest.setProductId("P123");
        placeOrderRequest.setAddressId("A123");
        placeOrderRequest.setAmount(100.0);

        // Perform the test
        OrderMgmtAPIResponse responseEntity = productService.placeOrderForProducts(userId, placeOrderRequest);

        // Assertions
        assertEquals(true, responseEntity.getSuccess());
    }

    @Test
    void placeOrderForProducts_Exception() throws Exception {
        // Mocking the downstream integration service
        when(downStreamIntegration.informOrderInitiation(anyString(), any(OrderInitiationRequest.class)))
                .thenThrow(new Exception("Some error occurred"));

        // Test parameters
        String userId = "U123";
        PlaceOrderRequest placeOrderRequest = new PlaceOrderRequest();
        placeOrderRequest.setProductId("P123");
        placeOrderRequest.setAddressId("A123");
        placeOrderRequest.setAmount(100.0);

        // Perform the test and assert the exception
        assertThrows(Exception.class,
                () -> productService.placeOrderForProducts(userId, placeOrderRequest));
    }

    @Test
    void getOrderStatus_Success() {
        // Mocking the order products mapping
        OrderProductsMapping orderProductsMapping = new OrderProductsMapping();
        orderProductsMapping.setProductsStatus(OrderStatusEnum.INITIATED);

        when(orderProductsMappingDao.findByOrderId(anyString()))
                .thenReturn(orderProductsMapping);

        // Test parameters
        String userId = "U123";
        String orderId = "O123";

        // Perform the test
        OrderMgmtAPIResponse responseEntity = productService.getOrderStatus(userId, orderId);

        // Assertions
        assertEquals(true, responseEntity.getSuccess());
        assertEquals("Order placed successfully", ((Map<String, Object>) responseEntity.getData()).get("message"));
    }

}