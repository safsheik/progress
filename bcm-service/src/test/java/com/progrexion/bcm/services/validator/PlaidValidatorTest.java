package com.progrexion.bcm.services.validator;

import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.progrexion.bcm.common.exceptions.BCMModuleException;
import com.progrexion.bcm.common.exceptions.BCMModuleExceptionEnum;
import com.progrexion.bcm.common.model.v1.LeaseResponseModel;
import com.progrexion.bcm.common.model.v1.PaymentAccountRequestModel;
import com.progrexion.bcm.common.properties.RentTrackConfigProperties;
import com.progrexion.bcm.model.entities.BCMCustomer;
import com.progrexion.bcm.services.util.TestServiceUtils;
import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;

@RunWith(DataProviderRunner.class)
public class PlaidValidatorTest {
	
	@InjectMocks
	private PlaidValidator validator;
	
	@Mock
	RentTrackConfigProperties property;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		when(property.getPlaidPublicTokenPrefix()).thenReturn("public-sandbox");
	}
	
	@DataProvider
	public static Object[][] requestValidation() {
		PaymentAccountRequestModel validRequest = constructRequest("nopay",
				"public-sandbox-fd0c258c-9af9-4358-8fd8-7e19e2207b90");

		PaymentAccountRequestModel validTokenRequest = constructRequest("nopay",
				"public-sandbox-fd0c258c-9af9-4358-8fd8-7e19e22gdgdf0");

 		return new Object[][] { { validRequest, null, null },
				{ validTokenRequest, null, "public_token" } };

	}
	
	
	@Test
	@UseDataProvider("requestValidation")
	public void testValidation(PaymentAccountRequestModel request, BCMModuleExceptionEnum exceptionEnum, String errorText) {
		validator.validatePaymentAccountRequest(request);
	}

	
	@Test(expected = BCMModuleException.class)
	@UseDataProvider("requestValidation")
	public void testValidationTokenRequestMissing(PaymentAccountRequestModel request, BCMModuleExceptionEnum exceptionEnum, String errorText) {
		request.setPublicToken(null);
		validator.validatePaymentAccountRequest(request);
	}
	
	@Test(expected = BCMModuleException.class)
	@UseDataProvider("requestValidation")
	public void testValidationTokenRequestInvalid(PaymentAccountRequestModel request, BCMModuleExceptionEnum exceptionEnum, String errorText) {
		request.setPublicToken("public-gfdgdfgd");
		validator.validatePaymentAccountRequest(request);
	}
	
	private static PaymentAccountRequestModel constructRequest(String payType,String publicToken)
	{
		PaymentAccountRequestModel requestModel = new PaymentAccountRequestModel();
		requestModel.setPayType(payType);
		requestModel.setPublicToken(publicToken);	
		return requestModel;
	}
	@Test(expected = BCMModuleException.class)
	public void testValidationLeaseTransactionFinder() {
		LeaseResponseModel leaseResponseModel = null;
		validator.validateLeaseTransactionFinder(leaseResponseModel);
	}
	
	@Test
	public void testValidationLeaseTransactionFinderInfo() {
		LeaseResponseModel leaseResponseModel = new LeaseResponseModel();
		leaseResponseModel.setBackReportStartAt("example");
		validator.validateLeaseTransactionFinder(leaseResponseModel);
	}
	
	@Test(expected = BCMModuleException.class)
	public void testExceptionValidateTransactionFinderId() {
		Long id = null;
		validator.validateTransactionFinderId(id);
	}
	
	@Test
	public void testValidateTransactionFinderId() {
		Long id = 100l;
		validator.validateTransactionFinderId(id);
	}
	
	@Test(expected = BCMModuleException.class)
	public void testExceptionValidatePaymentAccountId()
	{
		BCMCustomer customer =TestServiceUtils.getCustomerObject();
		customer.setPaymentAccountId(null);
		validator.validatePaymentAccountId(customer);
	}
	
	@Test
	public void testvalidatePaymentAccountId()
	{
		BCMCustomer customer =TestServiceUtils.getCustomerObject();
		validator.validatePaymentAccountId(customer);
	}
	

}