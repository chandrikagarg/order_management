package com.intuit.ordermanagement;

import com.intuit.ordermanagement.core.dao.OrderProductsMappingDao;
import com.intuit.ordermanagement.core.dao.OrderUserDetailsMappingDao;
import com.intuit.ordermanagement.core.entities.OrderProductsMapping;
import com.intuit.ordermanagement.core.entities.OrderUserDetailsMapping;
import com.intuit.ordermanagement.integrations.request.CallbackPaymentRequest;
import com.intuit.ordermanagement.integrations.request.OrderInitiationRequest;
import com.intuit.ordermanagement.integrations.request.OrderSubmitRequest;
import com.intuit.ordermanagement.integrations.request.PlaceOrderRequest;
import com.intuit.ordermanagement.integrations.response.DownStreamServiceBaseResponse;
import com.intuit.ordermanagement.integrations.response.PriceDetailsResponse;
import com.intuit.ordermanagement.integrations.response.TaxDetailsResponse;
import com.intuit.ordermanagement.integrations.service.IDownStreamIntegration;
import com.intuit.ordermanagement.service.ProductService;
import com.intuit.ordermanagement.service.enums.OrderStatusEnum;
import com.intuit.ordermanagement.service.enums.PaymentStatusEnum;
import com.intuit.ordermanagement.service.response.OrderMgmtAPIResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class OrderManagementApplicationTests {

//	@Test
//	void contextLoads() {
//	}


	@Mock
	private IDownStreamIntegration downStreamIntegration;

	@Mock
	private OrderUserDetailsMappingDao orderUserDetailsMappingDao;

	@Mock
	private OrderProductsMappingDao orderProductsMappingDao;

	@InjectMocks
	private ProductService productService;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testFindFinalPriceForProducts() throws Exception {
		// Prepare test data
		String productId = "123";
		String userId = "456";
		String addressId = "789";
		PriceDetailsResponse priceDetailsResponse = new PriceDetailsResponse();
		PriceDetailsResponse.DataObject dataObject = new PriceDetailsResponse.DataObject();
		dataObject.setBasePrice(10.0);
		priceDetailsResponse.setDataObject(dataObject);

		TaxDetailsResponse taxDetailsResponse = new TaxDetailsResponse();
		TaxDetailsResponse.DataObject taxDataObject = new TaxDetailsResponse.DataObject();
		taxDataObject.setTaxes(1.0);
		taxDetailsResponse.setDataObject(taxDataObject);


		// Mock the behavior of the downStreamIntegration
		when(downStreamIntegration.getBasePriceForProduct(eq(productId), eq(userId))).thenReturn(priceDetailsResponse);
		when(downStreamIntegration.getTaxDetailsForProduct(eq(10.0), eq(productId), eq(userId), eq(addressId))).thenReturn(taxDetailsResponse);

		// Call the method under test
		OrderMgmtAPIResponse response = productService.findFinalPriceForProducts(productId, userId, addressId);

		// Verify the result
		assertEquals(10.0, ((Map<String, Object>) response.getData()).get("basePrice"));
		assertEquals(1.0, ((Map<String, Object>) response.getData()).get("tax"));
		assertEquals(11.0, ((Map<String, Object>) response.getData()).get("totalPrice"));
	}

	@Test
	void testPlaceOrderForProducts_Success() throws Exception {
		// Prepare test data
		String userId = "123";
		PlaceOrderRequest placeOrderRequest = new PlaceOrderRequest();
		placeOrderRequest.setProductId("456");
		placeOrderRequest.setAmount(10.0);

		// Mock the behavior of the orderProductsMappingDao and downStreamIntegration
		OrderProductsMapping savedOrderProductsMapping = new OrderProductsMapping();
		when(orderProductsMappingDao.save(any(OrderProductsMapping.class))).thenReturn(savedOrderProductsMapping);

		OrderUserDetailsMapping savedOrderUserDetailsMapping = new OrderUserDetailsMapping();
		when(orderUserDetailsMappingDao.save(any(OrderUserDetailsMapping.class))).thenReturn(savedOrderUserDetailsMapping);

		DownStreamServiceBaseResponse downstreamResponse = new DownStreamServiceBaseResponse();
		downstreamResponse.setSuccess(true);
		when(downStreamIntegration.informOrderInitiation(eq(userId), any(OrderInitiationRequest.class)))
				.thenReturn(downstreamResponse);

		// Mock the behavior of the downStreamIntegration.getBasePriceForProduct() method
		PriceDetailsResponse priceDetailsResponse = new PriceDetailsResponse();
		PriceDetailsResponse.DataObject dataObject = new PriceDetailsResponse.DataObject();
		dataObject.setBasePrice(10.0);
		priceDetailsResponse.setDataObject(dataObject);
		when(downStreamIntegration.getBasePriceForProduct(eq(placeOrderRequest.getProductId()), eq(userId)))
				.thenReturn(priceDetailsResponse);

		// Call the method under test
		OrderMgmtAPIResponse response = productService.placeOrderForProducts(userId, placeOrderRequest);

		// Verify the result
		assertTrue(response.getSuccess());
		assertNotNull(response.getData());
//        assertEquals(savedOrderProductsMapping.getOrderId(), ((Map<String, Object>) response.getData()).get("orderId"));
//        assertEquals(savedOrderUserDetailsMapping.getRequestId(), ((Map<String, Object>) response.getData()).get("requestId"));
	}


	@Test
	void testPlaceOrderForProducts_Failure() throws Exception {
		// Prepare test data
		String userId = "123";
		PlaceOrderRequest placeOrderRequest = new PlaceOrderRequest();
		placeOrderRequest.setProductId("456");
		placeOrderRequest.setAmount(10.0);

		// Mock the behavior of the orderProductsMappingDao and downStreamIntegration
		when(orderProductsMappingDao.save(any(OrderProductsMapping.class))).thenReturn(new OrderProductsMapping());
		when(orderUserDetailsMappingDao.save(any(OrderUserDetailsMapping.class))).thenReturn(new OrderUserDetailsMapping());
		DownStreamServiceBaseResponse downStreamResponse = new DownStreamServiceBaseResponse();
		downStreamResponse.setSuccess(false);
		when(downStreamIntegration.informOrderInitiation(eq(userId), any(OrderInitiationRequest.class)))
				.thenReturn(downStreamResponse);

		// Call the method under test
		OrderMgmtAPIResponse response = productService.placeOrderForProducts(userId, placeOrderRequest);

		// Verify the result
		assertFalse(response.getSuccess());
		assertNotNull(response.getMessage());
	}


	@Test
	void testUpdatePaymentStatus_Success() throws Exception {
		// Prepare test data
		String userId = "123";
		CallbackPaymentRequest callbackPaymentRequest = new CallbackPaymentRequest();
		callbackPaymentRequest.setPaymentStatus(PaymentStatusEnum.SUCCESS.name());
		callbackPaymentRequest.setRequestId("456");

		OrderUserDetailsMapping orderUserDetailsMapping = new OrderUserDetailsMapping();
		orderUserDetailsMapping.setOrderId("789");

		OrderProductsMapping orderProductsMapping = new OrderProductsMapping();
		orderProductsMapping.setProductsStatus(OrderStatusEnum.PAYMENT_REQ_ACK);

		// Mock the behavior of the orderUserDetailsMappingDao and orderProductsMappingDao
		when(orderUserDetailsMappingDao.findByRequestId(eq(callbackPaymentRequest.getRequestId()))).thenReturn(orderUserDetailsMapping);
		when(orderProductsMappingDao.findByOrderId(eq(orderUserDetailsMapping.getOrderId()))).thenReturn(orderProductsMapping);
		DownStreamServiceBaseResponse downStreamResponse = new DownStreamServiceBaseResponse();
		downStreamResponse.setSuccess(false);
		when(downStreamIntegration.submitOrderForBilling(any(OrderSubmitRequest.class), eq(userId)))
				.thenReturn(downStreamResponse);

		// Call the method under test
		OrderMgmtAPIResponse response = productService.updatePaymentStatus(userId, callbackPaymentRequest);

		// Verify the result
		assertTrue(response.getSuccess());
	}
}
