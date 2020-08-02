package com.progrexion.bcm.services.v1.impl;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.progrexion.bcm.common.exceptions.BCMModuleException;
import com.progrexion.bcm.common.exceptions.BCMModuleExceptionEnum;
import com.progrexion.bcm.common.model.v1.CancelSubscriptionMessageRequestModel;
import com.progrexion.bcm.common.model.vendor.VendorSubscriptionResponseModelV2;
import com.progrexion.bcm.common.processor.VendorProcessor;
import com.progrexion.bcm.messagingconfig.enums.BCMVendorEnum;
import com.progrexion.bcm.model.entities.BCMCustomer;
import com.progrexion.bcm.model.repositories.CustomerRepository;
import com.progrexion.bcm.renttrack.client.RentTrackClient;
import com.progrexion.bcm.renttrack.serviceimpl.RentTrackProcessor;
import com.progrexion.bcm.services.factory.VendorProcessorFactory;
import com.progrexion.bcm.services.handler.VendorAPILogHandler;

import lombok.extern.slf4j.Slf4j;

@RunWith(MockitoJUnitRunner.class)
@Slf4j
public class MessagingServiceImplTest {

	@InjectMocks
	private MessagingServiceImpl messagingService;// = new MessagingServiceImpl();

	@Mock
	private VendorProcessorFactory vendorProcessorFactory;

	@Mock
	private VendorAPILogHandler logHandler;

	@Mock
	private CustomerRepository customerRepository;

	@Mock
	private VendorProcessor vendorProcessor;

	@Mock
	private RentTrackProcessor rentTrackProcessor;

	@Mock
	private RentTrackClient rentTrackClient;

	private CancelSubscriptionMessageRequestModel reqModel;

	private BCMCustomer customer;

	private Map<Long, BCMCustomer> testCustomers = new HashMap<>();

	List<VendorSubscriptionResponseModelV2> subscriptions = new ArrayList<>();
	String pbsMessage ;

	@Before
	public void setUp() throws Exception {
		Long ucid = 101L;
		String brand = "CCOM";
		List<Long> mergedUcids = new ArrayList<>();
		reqModel = new CancelSubscriptionMessageRequestModel();
		reqModel.setUcid(ucid);
		reqModel.setBrand(brand);
		reqModel.setMergedUcids(mergedUcids);
		pbsMessage = "pbsMessage";
		customer = new BCMCustomer();
		customer.setUcId(ucid);
		customer.setBrand(brand);
		customer.setAccessToken("787899nkkkhfusfsdfd");
		customer.setRefreshToken("fsdfnsfsldfi97800");
		customer.setCustEmail("test101@abc.com");
		customer.setTransactionFinderId(101L);

		testCustomers.put(101L, customer);

		VendorSubscriptionResponseModelV2 subcription = new VendorSubscriptionResponseModelV2();
		subcription.setId(101L);
		subscriptions.add(subcription);

	}

	@Test
	public void processCancelSubscriptionMessageSuccessTest()
			throws JsonParseException, JsonMappingException, IOException {
		log.info("TEST::MessagingServiceImplTest::processCancelSubscriptionMessageSuccessTest");
		
		when(customerRepository.findFirstByUcIdInAndBrandOrderByCreatedDateDesc(Mockito.anyList(), Mockito.anyString()))
				.thenReturn(customer);
		when(vendorProcessorFactory.getVendorProcessor(BCMVendorEnum.RENTTRACK)).thenReturn(rentTrackProcessor);
		when(rentTrackProcessor.getActiveSubscriptions(Mockito.anyLong(), Mockito.any()))
				.thenReturn(subscriptions);
		when(rentTrackProcessor.deleteSubscription(Mockito.anyLong(), Mockito.anyLong(),
				Mockito.any())).thenReturn(true);
		
		Boolean cancelSubscriptionStatus = messagingService.processCancelSubscriptionMessage(reqModel, pbsMessage);
		
		assertEquals(true, cancelSubscriptionStatus);
	}

	@Test
	public void processCancelSubscriptionMessageFailureTest()
			throws JsonParseException, JsonMappingException, IOException {
		log.info("TEST::MessagingServiceImplTest::processCancelSubscriptionMessageFailureTest");
		
		when(customerRepository.findFirstByUcIdInAndBrandOrderByCreatedDateDesc(Mockito.anyList(), Mockito.anyString()))
				.thenReturn(customer);
		when(vendorProcessorFactory.getVendorProcessor(BCMVendorEnum.RENTTRACK)).thenReturn(rentTrackProcessor);
		when(rentTrackProcessor.getActiveSubscriptions(Mockito.anyLong(), Mockito.any()))
				.thenReturn(subscriptions);
		when(rentTrackProcessor.deleteSubscription(Mockito.anyLong(), Mockito.anyLong(),
				Mockito.any())).thenReturn(false);
		
		Boolean cancelSubscriptionStatus = messagingService.processCancelSubscriptionMessage(reqModel,pbsMessage);
		
		assertEquals(false, cancelSubscriptionStatus);
	}

	@Test
	public void processCancelSubscriptionMessageInvaildCustomerTest() throws JsonParseException, JsonMappingException, IOException {
		log.info("TEST::MessagingServiceImplTest::processCancelSubscriptionMessageInvaildCustomerTest");
		
		when(customerRepository.findFirstByUcIdInAndBrandOrderByCreatedDateDesc(Mockito.anyList(), Mockito.anyString())).thenReturn(null);
		
		try {
			messagingService.processCancelSubscriptionMessage(reqModel, pbsMessage);
		} catch (BCMModuleException e) {
			assertEquals(BCMModuleExceptionEnum.CANNOT_FIND_CUSTOMER_BCM_MESSAGING_EXCEPTION.getCode(), e.getErrorCode());
		}
		
	}
	
	@Test
	public void processCancelSubscriptionMessageNoActiveSubscriptionsTest()
			throws JsonParseException, JsonMappingException, IOException {
		log.info("TEST::MessagingServiceImplTest::processCancelSubscriptionMessageNoActiveSubscriptionsTest");
		
		when(customerRepository.findFirstByUcIdInAndBrandOrderByCreatedDateDesc(Mockito.anyList(), Mockito.anyString()))
				.thenReturn(customer);
		when(vendorProcessorFactory.getVendorProcessor(BCMVendorEnum.RENTTRACK)).thenReturn(rentTrackProcessor);
		when(rentTrackProcessor.getActiveSubscriptions(Mockito.anyLong(), Mockito.any()))
				.thenReturn(new ArrayList<>());
		
		try {
			messagingService.processCancelSubscriptionMessage(reqModel, pbsMessage);
		} catch (BCMModuleException e) {
			assertEquals(BCMModuleExceptionEnum.NO_ACTIVE_SUBSCRIPTIONS_BCM_MESSAGING_EXCEPTION.getCode(), e.getErrorCode());
		}
	}

}
