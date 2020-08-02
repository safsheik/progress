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
import com.progrexion.bcm.common.model.v1.UtilityStatusRequestModel;
import com.progrexion.bcm.common.properties.RentTrackConfigProperties;
import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;

@RunWith(DataProviderRunner.class)
public class UtilityValidatorTest {

	@InjectMocks
	private UtilityValidator validator;
	
	@Mock
	RentTrackConfigProperties property;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		when(property.getUtilStatus()).thenReturn("current,finished");
		when(property.getUtilTypes()).thenReturn("GAS,WATER,ELECTRIC,WIRELESS");
	}
	
	@DataProvider
	public static Object[][] requestValidation() {
		UtilityStatusRequestModel validRequest = constructRequest("current",
				"WATER");

		UtilityStatusRequestModel validStatusRequest = constructRequest("current",
				"GAS");
		UtilityStatusRequestModel validOptForRequest = constructRequest("finished",
				"WATER");
		return new Object[][] { { validRequest, null, null },
				{ validStatusRequest, null, "status" }, { validOptForRequest, null, "optFor" },};

	}
	
	
	@Test
	@UseDataProvider("requestValidation")
	public void testValidation(UtilityStatusRequestModel request, BCMModuleExceptionEnum exceptionEnum, String errorText) {
		validator.validateUtiltiStatusRequestModel(request);
	}

	
	@Test(expected = BCMModuleException.class)
	@UseDataProvider("requestValidation")
	public void testValidationStatusMissing(UtilityStatusRequestModel request, BCMModuleExceptionEnum exceptionEnum, String errorText) {
		
		request.setStatus(null);
		validator.validateUtiltiStatusRequestModel(request);
	}
	
	@Test(expected = BCMModuleException.class)
	@UseDataProvider("requestValidation")
	public void testValidationStatusInvalid(UtilityStatusRequestModel request, BCMModuleExceptionEnum exceptionEnum, String errorText) {
		
		request.setStatus("invalid");
		validator.validateUtiltiStatusRequestModel(request);
	}
	
	
	@Test(expected = BCMModuleException.class)
	@UseDataProvider("requestValidation")
	public void testValidationOptForMissing(UtilityStatusRequestModel request, BCMModuleExceptionEnum exceptionEnum, String errorText) {
		
		request.setOptFor(null);
		validator.validateUtiltiStatusRequestModel(request);
	}
	
	@Test(expected = BCMModuleException.class)
	@UseDataProvider("requestValidation")
	public void testValidationOptForInvalid(UtilityStatusRequestModel request, BCMModuleExceptionEnum exceptionEnum, String errorText) {
		
		request.setOptFor("invalid");
		validator.validateUtiltiStatusRequestModel(request);
	}
	private static UtilityStatusRequestModel constructRequest(String status,String optFor)
	{
		UtilityStatusRequestModel requestModel =new UtilityStatusRequestModel();
		requestModel.setStatus(status);
		requestModel.setOptFor(optFor);
		return requestModel;
	}
	

}