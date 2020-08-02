package com.progrexion.bcm.services.validator;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.progrexion.bcm.common.exceptions.BCMModuleException;
import com.progrexion.bcm.common.exceptions.BCMModuleExceptionEnum;
import com.progrexion.bcm.common.model.v1.LeaseResponseModel;
import com.progrexion.bcm.common.model.v1.TransactionRequestModel;
import com.progrexion.bcm.common.model.vendor.VendorLeaseResponseModel;
import com.progrexion.bcm.common.model.vendor.VendorPaymentAccountResponseModel;
import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;

@RunWith(DataProviderRunner.class)
public class TransactionValidatorTest {
	
	private TransactionValidator validator = new TransactionValidator();
	
	@DataProvider
	public static Object[][] requestValidation() {
		TransactionRequestModel validRequest = constructRequest(15,10.0f);

		TransactionRequestModel validDueDayRequest = constructRequest(12,
				10.0f);
		
		return new Object[][] { { validRequest, null, null },
				{ validDueDayRequest, null, "dueDay" } };

	}
	
	
	@DataProvider
	public static Object[][] leaseRequestValidation() {

		VendorLeaseResponseModel vendorLeaseResponseModel = constructLeaseModel(101l);
		VendorLeaseResponseModel vendorLeaseResponseModel1 = constructLeaseModel(10142l);
		VendorLeaseResponseModel vendorLeaseResponseModel2 = constructLeaseModel(1014342l);
		
		return new Object[][] { { vendorLeaseResponseModel, null, null },
			{ vendorLeaseResponseModel1, null, "leaseId" },
			{ vendorLeaseResponseModel2, null, "leaseId" },};

	}
	
	@DataProvider
	public static Object[][] paymentAccountRequestValidation() {

		VendorPaymentAccountResponseModel vendorPaymentAccountResponseModel = constructPaymentAccountModel(1075761l);
		VendorPaymentAccountResponseModel vendorPaymentAccountResponseModel1 = constructPaymentAccountModel(10576l);
		VendorPaymentAccountResponseModel vendorPaymentAccountResponseModel2 = constructPaymentAccountModel(15761l);
		
		return new Object[][] { { vendorPaymentAccountResponseModel, null, null },
			{ vendorPaymentAccountResponseModel1, null, "id" },
			{ vendorPaymentAccountResponseModel2, null, "id" },};

	}
	
	@Test
	@UseDataProvider("requestValidation")
	public void testValidation(TransactionRequestModel request, BCMModuleExceptionEnum exceptionEnum, String errorText) {
		validator.validateTransactionFinder(request);
	}

	
	@Test(expected = BCMModuleException.class)
	@UseDataProvider("requestValidation")
	public void testValidationDueDayRequestMissing(TransactionRequestModel request, BCMModuleExceptionEnum exceptionEnum, String errorText) {
		
		request.setDueDay(45);
		validator.validateTransactionFinder(request);
	}
	
	@Test(expected = BCMModuleException.class)
	@UseDataProvider("requestValidation")
	public void testValidationRentAmountInvalid(TransactionRequestModel request, BCMModuleExceptionEnum exceptionEnum, String errorText) {
		
		request.setRentAmount(0f);
		validator.validateTransactionFinder(request);
	}
	
	@Test
	@UseDataProvider("leaseRequestValidation")
	public void testValidateLeaseModel(VendorLeaseResponseModel request, BCMModuleExceptionEnum exceptionEnum, String errorText) {
		validator.validateLeaseTransactionFinder(request);
	}

	
	@Test(expected = BCMModuleException.class)
	@UseDataProvider("leaseRequestValidation")
	public void testValidateLeaseModelMissing(VendorLeaseResponseModel request, BCMModuleExceptionEnum exceptionEnum, String errorText) {
		
		request = null;
		validator.validateLeaseTransactionFinder(request);
	}
	
	@Test
	@UseDataProvider("paymentAccountRequestValidation")
	public void testValidatePaymentAccountModel(VendorPaymentAccountResponseModel request, BCMModuleExceptionEnum exceptionEnum, String errorText) {
		validator.validatePaymentAccount(request);
	}

	
	@Test(expected = BCMModuleException.class)
	@UseDataProvider("paymentAccountRequestValidation")
	public void testValidatePaymentAccountMissing(VendorPaymentAccountResponseModel request, BCMModuleExceptionEnum exceptionEnum, String errorText) {
		
		request = null;
		validator.validatePaymentAccount(request);
	}
	private static TransactionRequestModel constructRequest(int dueDay,float rentAmount)
	{
		TransactionRequestModel requestModel =new TransactionRequestModel();
		requestModel.setDueDay(dueDay);	
		requestModel.setRentAmount(rentAmount);
		return requestModel;
	}
	private static VendorLeaseResponseModel constructLeaseModel(Long leaseId) {
		VendorLeaseResponseModel requestModel = new VendorLeaseResponseModel();
		requestModel.setLeaseId(leaseId);
		return requestModel;
	}
	
	private static VendorPaymentAccountResponseModel constructPaymentAccountModel(Long id) {
		VendorPaymentAccountResponseModel requestModel = new VendorPaymentAccountResponseModel();
		requestModel.setId(id);
		return requestModel;
	}
	
	@Test(expected = BCMModuleException.class)
	public void testValidationLeaseTransactionFinder() {
		LeaseResponseModel leaseResponseModel = null;
		validator.validateLeaseResponse(leaseResponseModel);
	}

}