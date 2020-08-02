package com.progrexion.bcm.services.validator;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.progrexion.bcm.common.exceptions.BCMModuleException;
import com.progrexion.bcm.common.exceptions.BCMModuleExceptionEnum;
import com.progrexion.bcm.common.model.v1.Address;
import com.progrexion.bcm.common.model.v1.Landlord;
import com.progrexion.bcm.common.model.v1.LeaseRequestModel;
import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;

@RunWith(DataProviderRunner.class)
public class LeaseValidatorTest {
	
	private LeaseValidator validator = new LeaseValidator();
	
	@DataProvider
	public static Object[][] requestValidation() {
		LeaseRequestModel validLeaseAdress1Request = constructLeaseRequest("RENTTRACK",
				constructAddress("Plot A5","PARK STREET","LAKE CITY","4900051","STATE","US"),5,
				constructLandlord("","John","7894561234","abc@test.com"),(float) 50000.00);
		
		LeaseRequestModel validLeaseCityRequest = constructLeaseRequest("RENTTRACK",
				constructAddress("Plot A5","address2","city","zip","STATE","US"),5,
				constructLandlord("","John","7894561234","abc@test.com"),(float) 50000.00);
		
		LeaseRequestModel validLeaseZipRequest = constructLeaseRequest("RENTTRACK",
				constructAddress("Plot A5","STREET","CITY","4900051","STATE","US"),5,
				constructLandlord("","John","","abc@test.com"),(float) 50000.00);

		return new Object[][] { { validLeaseAdress1Request, null, null }, { validLeaseCityRequest, null, "city" },
				{ validLeaseZipRequest, null, "address1" }, };

	}
	
	
	@Test
	@UseDataProvider("requestValidation")
	public void testValidation(LeaseRequestModel request, BCMModuleExceptionEnum exceptionEnum, String errorText) {
		validator.validateLeaseRequest(request);
	}

	
	@Test(expected = BCMModuleException.class)
	@UseDataProvider("requestValidation")
	public void testValidationAddress1Missing(LeaseRequestModel request, BCMModuleExceptionEnum exceptionEnum,
			String errorText) {
		
		request.setAddress(constructAddress(null,"STREET","CITY","4900051","STATE","US"));;
		validator.validateLeaseRequest(request);
	}
	
	@Test(expected = BCMModuleException.class)
	@UseDataProvider("requestValidation")
	public void testValidationCityMissing(LeaseRequestModel request, BCMModuleExceptionEnum exceptionEnum,
			String errorText) {
		
		request.setAddress(constructAddress("address1","STREET",null,"4900051","STATE","US"));;
		validator.validateLeaseRequest(request);
	}
	
	@Test(expected = BCMModuleException.class)
	@UseDataProvider("requestValidation")
	public void testValidationStateMissing(LeaseRequestModel request, BCMModuleExceptionEnum exceptionEnum,
			String errorText) {
		
		request.setAddress(constructAddress("address1","STREET","CITY","4900051",null,"US"));;
		validator.validateLeaseRequest(request);
	}
	
	@Test(expected = BCMModuleException.class)
	@UseDataProvider("requestValidation")
	public void testValidationZipMissing(LeaseRequestModel request, BCMModuleExceptionEnum exceptionEnum,
			String errorText) {
		
		request.setAddress(constructAddress("address1","STREET","CITY",null,"STATE","US"));;
		validator.validateLeaseRequest(request);
	}
	
	@Test(expected = BCMModuleException.class)
	@UseDataProvider("requestValidation")
	public void testValidationRent0(LeaseRequestModel request, BCMModuleExceptionEnum exceptionEnum,
			String errorText) {
		
		request.setRent(0f);
		validator.validateLeaseRequest(request);
	}
	
	
	@Test(expected = BCMModuleException.class)
	@UseDataProvider("requestValidation")
	public void testValidationDueDay(LeaseRequestModel request, BCMModuleExceptionEnum exceptionEnum,
			String errorText) {
		
		request.setDueDay(40);
		validator.validateLeaseRequest(request);
	}
	
	@Test(expected = BCMModuleException.class)
	@UseDataProvider("requestValidation")
	public void testValidationDueDayZero(LeaseRequestModel request, BCMModuleExceptionEnum exceptionEnum,
			String errorText) {
		
		request.setDueDay(0);
		validator.validateLeaseRequest(request);
	}
	
	
	@Test(expected = BCMModuleException.class)
	@UseDataProvider("requestValidation")
	public void testValidationAddress1MissingException(LeaseRequestModel request, BCMModuleExceptionEnum exceptionEnum, String errorText) {
		
		request.setAddress(null);;
		validator.validateLeaseRequest(request);
	}
	
	private static LeaseRequestModel constructLeaseRequest(String vendor,Address address,int dueDay,Landlord landLord,Float rent)
	{
		LeaseRequestModel requestModel =new LeaseRequestModel();
		requestModel.setAddress(address);
		requestModel.setDueDay(dueDay);
		requestModel.setLandlord(landLord);
		requestModel.setRent(rent);
		return requestModel;
	}
	
	
	private static Address constructAddress(String address1, String address2, String city, String zip, String state, String country)
	{
		Address address =new Address(address1,address2,city,zip,state,country);
		return address;

	}
	
	private static Landlord constructLandlord(String type, String name, String phone, String email)
	{
		Landlord landLord =new Landlord(type,name,phone,email);
		return landLord;

	}

}