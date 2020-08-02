package com.progrexion.bcm.services.builder;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.progrexion.bcm.common.model.v1.PaymentAccountRequestModel;
import com.progrexion.bcm.common.model.vendor.VendorPaymentAccountRequestModel;
import com.progrexion.bcm.common.properties.RentTrackConfigProperties;

@RunWith(MockitoJUnitRunner.class)
public class PlaidRequestBuilderTest {

	@Mock
	private ObjectMapper mockObjectMapper;
	@InjectMocks
	private PlaidRequestBuilder builder;
	@Mock
	private RentTrackConfigProperties property;
	
	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		when(property.getPaymentAccPayType()).thenReturn("nopay");

	}
	@Test
	public void test_buildPlaidVendorPaymentAccountRequestModel() {
		PaymentAccountRequestModel reqModel =  new PaymentAccountRequestModel();
        reqModel.setPublicToken("public-sandbox-34345");
        reqModel.setPlaidAccountId("asd3465");
		VendorPaymentAccountRequestModel response = builder.buildPlaidVendorPaymentAccountRequestModel(reqModel);
		assertNotNull(response);
	}
	
}
