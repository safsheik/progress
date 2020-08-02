package com.progrexion.bcm.services.validator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import com.progrexion.bcm.common.exceptions.BCMModuleException;
import com.progrexion.bcm.common.exceptions.BCMModuleExceptionEnum;
import com.progrexion.bcm.common.model.v1.SubscriptionResponseModel;
import com.progrexion.bcm.model.entities.BCMCustomer;
import com.progrexion.bcm.services.v1.SubscriptionService;

@RunWith(MockitoJUnitRunner.class)
public class BCMValidatorTest {

	@InjectMocks
	private BCMValidator validator;
	private BCMCustomer customer;
	@Mock
	private SubscriptionService service;
	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);

	}

	
	@Test
	public void testValidation() {
		SubscriptionResponseModel[] responseMock = new SubscriptionResponseModel[3];
		when(service.getSubscriptionsInfo(Mockito.any())).thenReturn(responseMock);	
		validator.validateCustomer(customer);
		assertNotNull(validator);
	}


	@Test
	public void testValidationException() {
		SubscriptionResponseModel[] responseMock = new SubscriptionResponseModel[0];
		when(service.getSubscriptionsInfo(Mockito.any())).thenReturn(responseMock);	
		try {
		validator.validateCustomer(customer);
		}
		catch (BCMModuleException e) {
			
			assertEquals(BCMModuleExceptionEnum.CUSTOMER_SUBSCRIPTION_FAILED.getCode(), e.getErrorCode());
			}
		
	}
	
	@Test
	public void testVerifyCustomerSubscription() {
		SubscriptionResponseModel[] responseMock = new SubscriptionResponseModel[3];
		when(service.getSubscriptionsInfo(Mockito.any())).thenReturn(responseMock);	
		validator.verifyCustomerSubscription(customer);
		assertNotNull(validator);
	}
	
	@Test
	public void testGetCustomerRTSubscription() {
		SubscriptionResponseModel[] responseMock = new SubscriptionResponseModel[3];
		when(service.getSubscriptionsInfo(Mockito.any())).thenReturn(responseMock);	
		validator.getCustomerRTSubscription(customer);
		assertNotNull(validator);
	}

}