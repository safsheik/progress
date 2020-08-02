package com.progrexion.bcm.services.builder;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.progrexion.bcm.common.model.v1.UtilityStatusRequestModel;
import com.progrexion.bcm.common.model.vendor.VendorUtilityStatusRequestModel;

@RunWith(MockitoJUnitRunner.class)
public class UtilityRequestBuilderTest {

	@Mock
	private ObjectMapper mockObjectMapper;

	private UtilityRequestBuilder builder = new UtilityRequestBuilder();


	@Test
	public void test_buildVendorLeaseRequestModel() {
		UtilityStatusRequestModel reqModel = new UtilityStatusRequestModel();
		reqModel.setStatus("current");
		reqModel.setOptFor("GAS");
		VendorUtilityStatusRequestModel response = builder.buildUtiltyStatusRequestModel(reqModel);
		assertNotNull(response);
	}
	
}
