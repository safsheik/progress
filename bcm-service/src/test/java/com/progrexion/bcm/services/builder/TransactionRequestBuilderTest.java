package com.progrexion.bcm.services.builder;

import static org.junit.Assert.assertNotNull;

import java.time.LocalDate;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.progrexion.bcm.common.model.v1.MatchTransactionRequestModel;
import com.progrexion.bcm.common.model.v1.TransactionRequestModel;
import com.progrexion.bcm.common.model.vendor.VendorMatchTransactionRequestModel;
import com.progrexion.bcm.common.model.vendor.VendorTransactionRequestModel;
import com.progrexion.bcm.services.util.TestServiceUtils;

@RunWith(MockitoJUnitRunner.class)
public class TransactionRequestBuilderTest {

	@Mock
	private ObjectMapper mockObjectMapper;

	private TransactionRequestBuilder builder = new TransactionRequestBuilder();


	@Test
	public void test_buildVendorMatchTransactionRequestModel() {
		MatchTransactionRequestModel reqModel =  new MatchTransactionRequestModel();
	        reqModel.setTransactionID("ojEBr8gApyspayb3EeZ1Uy8dNmyRz6FRe6E6m");
	        reqModel.setAmount("6.33");
	        reqModel.setName("Uber 072515 SF**POOL**");
	        reqModel.setCategoryId("22006001");
	        reqModel.setDate(LocalDate.parse("2019-10-15"));
	        VendorMatchTransactionRequestModel response = builder.buildVendorMatchTransactionRequestModel(reqModel);
		assertNotNull(response);
	}
	
	@Test
	public void test_buildVendorTransactionRequestModel() {
		TransactionRequestModel reqModel =  TestServiceUtils.getTransactionRequestModelObj();
	        VendorTransactionRequestModel response = builder.buildVendorTransactionFinderRequestModel(reqModel);
		assertNotNull(response);
	}
	
}
