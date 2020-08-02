package com.progrexion.bcm.services.builder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import com.progrexion.bcm.common.exceptions.BCMModuleException;
import com.progrexion.bcm.common.exceptions.BCMModuleExceptionEnum;
import com.progrexion.bcm.common.model.vendor.VendorSubscriptionRequestModel;
import com.progrexion.bcm.common.properties.RentTrackConfigProperties;
import com.progrexion.bcm.services.constants.TestConstants;

@RunWith(MockitoJUnitRunner.class)
public class SubscriptionRequestBuilderTest {

	@InjectMocks
	private SubscriptionRequestBuilder builder;
	@Mock
	RentTrackConfigProperties property;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		when(property.getSubscriptionDetails()).thenReturn("CCOM_PLAN:consumer_lite,CCOM_PROMO:CREDITCOMFREE");
	}


	@Test
	public void test_buildVendorLeaseRequestModel() {
	
		VendorSubscriptionRequestModel response = builder.buildVendorSubscriptionRequestModel(TestConstants.BRAND_CCOM);
		assertNotNull(response);
	}
	
	@Test
	public void test_buildVendorLeaseRequestModelException() {
		when(property.getSubscriptionDetails()).thenReturn("CCOM_PROMO:CREDITCOMFREE");
		try
		{
		builder.buildVendorSubscriptionRequestModel(TestConstants.BRAND_CCOM);
		}
		catch(BCMModuleException e)
		{
			assertEquals(BCMModuleExceptionEnum.SUBSCRIPTION_INVALID_PROPERTY.getCode(), e.getErrorCode());
		}
	}
	
	@Test
	public void test_buildVendorLeaseRequestModelException2() {
		when(property.getSubscriptionDetails()).thenReturn(null);
		try
		{
		builder.buildVendorSubscriptionRequestModel(TestConstants.BRAND_CCOM);
		}
		catch(BCMModuleException e)
		{
			assertEquals(BCMModuleExceptionEnum.SUBSCRIPTION_INVALID_PROPERTY.getCode(), e.getErrorCode());
		}
	}
	
}
