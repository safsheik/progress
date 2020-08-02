package com.progrexion.bcm.services.v1.impl;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;

import com.progrexion.bcm.common.model.v1.SubscriptionRequestModel;
import com.progrexion.bcm.common.model.v1.SubscriptionResponseModel;
import com.progrexion.bcm.common.model.vendor.VendorSubscriptionResponseModel;
import com.progrexion.bcm.common.processor.VendorProcessor;
import com.progrexion.bcm.model.entities.BCMCustomer;
import com.progrexion.bcm.services.adapter.CustomerMasterAdapter;
import com.progrexion.bcm.services.builder.SubscriptionRequestBuilder;
import com.progrexion.bcm.services.constants.TestConstants;
import com.progrexion.bcm.services.factory.VendorProcessorFactory;
import com.progrexion.bcm.services.handler.VendorAPILogHandler;
import com.progrexion.bcm.services.persistence.PersistenceService;
import com.progrexion.bcm.services.util.TestServiceUtils;
import com.progrexion.bcm.services.validator.SubscriptionValidator;




@RunWith(MockitoJUnitRunner.class)
public class SubscriptionServiceImplTest {

	@InjectMocks
	private SubscriptionServiceImpl subscriptionService;
	@Mock
	private ModelMapper modelMapper;
	@Mock
	private VendorProcessorFactory vendorProcessorFactory;
	@Mock
	private VendorProcessor vendor;
	@Mock
	private SubscriptionValidator validator;
	@Mock
	private CustomerMasterAdapter customerMasterAdapter;
	@Mock
	private PersistenceService persistenceService;
	@Mock
	private SubscriptionRequestBuilder subscriptionRequestBuilder;
	@Mock
	private VendorAPILogHandler logHandler;

	Set<Long> customerAllUcids = new HashSet<>();
	private BCMCustomer customer;
    private SubscriptionRequestModel reqModel;
    private SubscriptionResponseModel resModel;
	private Long ucid;
	private String brand;
    @Before
    public void setUp() throws Exception {

    	MockitoAnnotations.initMocks(this);
        ucid = TestConstants.UCID;
        brand = TestConstants.BRAND_CCOM;
        reqModel =  TestServiceUtils.getSubscriptionRequestModelObj();
        customer = TestServiceUtils.getCustomerObject();
    }
	
	@Test
	public void processCreatePaymentAccountSuccessTest(){
		setMockResponses();
		VendorSubscriptionResponseModel response = new VendorSubscriptionResponseModel();
		when(vendor.createSubscription(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(response);
		resModel = subscriptionService.createSubscription(ucid, brand, reqModel);
		assertNotNull(resModel);
	}	
	
	@Test
	public void processCreatePaymentAccountSuccessFromCustomerServiceTest(){
		setMockResponses();
		VendorSubscriptionResponseModel response = new VendorSubscriptionResponseModel();
		when(vendor.createSubscription(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(response);
		response = subscriptionService.processSubscriptionRequest(customer, vendor);
		assertNotNull(response);
	}	
	
	@Test
	public void processgetSubscriptionsInfoSuccessTest(){
		setMockResponses();
		VendorSubscriptionResponseModel[] responseMock = new VendorSubscriptionResponseModel[3];
		when(vendor.getSubscriptionsInfo(Mockito.any(), Mockito.any())).thenReturn(responseMock);		
		SubscriptionResponseModel[] response = subscriptionService.getSubscriptionsInfo(ucid, brand);
		assertNotNull(response);
	}	
	@Test
	public void processgetSubscriptionsInfoFromCustomerSuccessTest(){
		setMockResponses();
		VendorSubscriptionResponseModel[] responseMock = new VendorSubscriptionResponseModel[3];
		when(vendor.getSubscriptionsInfo(Mockito.any(), Mockito.any())).thenReturn(responseMock);		
		SubscriptionResponseModel[] response = subscriptionService.getSubscriptionsInfo(customer);
		assertNotNull(response);
	}	
	
	private void setMockResponses()
	{
		customerAllUcids.add(ucid);
		when(customerMasterAdapter.getCustomerUCIDs(ucid)).thenReturn(customerAllUcids);
		when(persistenceService.getLatestActiveCustomer(Mockito.any(), Mockito.any())).thenReturn(customer);
		when(vendorProcessorFactory.getVendorProcessor(Mockito.any())).thenReturn(vendor);
	}
	 
}
