package com.progrexion.bcm.services.builder;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.progrexion.bcm.common.model.v1.Address;
import com.progrexion.bcm.common.model.v1.Landlord;
import com.progrexion.bcm.common.model.v1.LeaseRequestModel;
import com.progrexion.bcm.common.model.vendor.VendorLeaseRequestModel;

@RunWith(MockitoJUnitRunner.class)
public class LeaseRequestBuilderTest {

	@Mock
	private ObjectMapper mockObjectMapper;

	private LeaseRequestBuilder builder = new LeaseRequestBuilder();


	@Test
	public void test_buildVendorLeaseRequestModel() {
		LeaseRequestModel reqModel =  new LeaseRequestModel();
        Address address = new Address("Plot A5","STREET","CITY","4900051","STATE","US");
        Landlord landlord =new Landlord("","John","7894561234","abc@test.com");
        reqModel.setAddress(address);
        reqModel.setDueDay(1);
        reqModel.setLandlord(landlord);
        reqModel.setRent((float)500.00);
        VendorLeaseRequestModel response = builder.buildVendorLeaseRequestModel(reqModel);
		assertNotNull(response);
	}
	
}
