package com.progrexion.bcm.services.v1.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;

import com.progrexion.bcm.common.exceptions.BCMModuleException;
import com.progrexion.bcm.common.exceptions.BCMModuleExceptionEnum;
import com.progrexion.bcm.common.model.v1.UtilityStatusRequestModel;
import com.progrexion.bcm.common.model.vendor.VendorOrdersResponseModel;
import com.progrexion.bcm.common.model.vendor.VendorUtilityDetailsResponseModel;
import com.progrexion.bcm.common.model.vendor.VendorUtilityResponseModel;
import com.progrexion.bcm.common.processor.VendorProcessor;
import com.progrexion.bcm.model.entities.BCMCustomer;
import com.progrexion.bcm.services.adapter.CustomerMasterAdapter;
import com.progrexion.bcm.services.builder.UtilityRequestBuilder;
import com.progrexion.bcm.services.factory.VendorProcessorFactory;
import com.progrexion.bcm.services.handler.VendorAPILogHandler;
import com.progrexion.bcm.services.persistence.PersistenceService;
import com.progrexion.bcm.services.util.TestServiceUtils;
import com.progrexion.bcm.services.validator.UtilityValidator;




@RunWith(MockitoJUnitRunner.class)
public class UtilityServiceImplTest {

	@InjectMocks
	private UtilityServiceImpl utilityService;
	@Mock
	private ModelMapper modelMapper;
	@Mock
	private VendorProcessorFactory vendorProcessorFactory;
	@Mock
	private VendorProcessor vendor;
	@Mock
	private UtilityValidator validator;
	@Mock
	private CustomerMasterAdapter customerMasterAdapter;
	@Mock
	private PersistenceService persistenceService;
	@Mock
	private UtilityRequestBuilder utilityRequestBuilder;
	@Mock
	private VendorAPILogHandler logHandler;

	Set<Long> customerAllUcids = new HashSet<>();
	private BCMCustomer customer;
	private Long ucid;
	private String brand;
	private VendorUtilityResponseModel[] responseModelArray;
	private VendorUtilityDetailsResponseModel[] responseDetailsModelArray;
	private List<VendorOrdersResponseModel> vendorOrdersResponseModelList;

	private Long utilId;
	private UtilityStatusRequestModel utilityStatusRequestModel;
    @Before
    public void setUp() throws Exception {

    	MockitoAnnotations.initMocks(this);
        ucid = (long) 101;
        brand = "CCOM";
        utilId = (long) 10002;
		utilityStatusRequestModel = TestServiceUtils.getUtilityStatusRequestModelObj();
		responseModelArray = new VendorUtilityResponseModel[1];
		responseDetailsModelArray = new VendorUtilityDetailsResponseModel[1];
		
		VendorUtilityDetailsResponseModel vendorUtilityDetailsResponse = new VendorUtilityDetailsResponseModel();
		vendorUtilityDetailsResponse.setId(1001l);
		
		VendorUtilityResponseModel vendorUtilityResponse= new VendorUtilityResponseModel();
		vendorUtilityResponse.setId(1001l);
		responseDetailsModelArray[0] = vendorUtilityDetailsResponse;
		responseModelArray[0]=vendorUtilityResponse;
		 customer = TestServiceUtils.getCustomerObject();

    }
	
	@Test
	public void processGetAllUtilitiesSuccessTest(){
		
		setMockResponses();
		when(vendor.getAllUtilities(Mockito.anyLong(), Mockito.any())).thenReturn(responseModelArray);		
		assertNull(utilityService.getAllUtilities(ucid,brand));
	}
	
	@Test
	public void processGetUtilityDetailsSuccessTest() {
	
		setMockResponses();
		when(vendor.getUtilityDetails(Mockito.anyLong(),Mockito.anyLong(), Mockito.any())).thenReturn(responseDetailsModelArray);		
		assertNull(utilityService.getUtilityDetails(ucid,brand,utilId));
	}

	@Test
	public void processUpdateUtilityStatusSuccessTest() {
		setMockResponses();
		when(vendor.updateUtiltyStatusRequest(Mockito.any(),Mockito.any(), Mockito.any(), Mockito.any()))
		.thenReturn(true);		
		Boolean isUpdated =  utilityService.updateUtiltyStatus(ucid, brand, utilityStatusRequestModel);
		assertEquals(true,isUpdated);
	}
	
	@Test
	public void processUpdateUtilityStatusExceptionTest() {
		setMockResponses();	
		utilityStatusRequestModel.setOptFor("UNKNOWN");
		try{
			utilityService.updateUtiltyStatus(ucid, brand, utilityStatusRequestModel);
		}
		catch (BCMModuleException e) {
			
			assertEquals(BCMModuleExceptionEnum.PATCH_UTIL_INVALID_UTIL_TYPE.getCode(), e.getErrorCode());
			}
	}
	private void setMockResponses()
	{
		customerAllUcids.add(ucid);
		when(customerMasterAdapter.getCustomerUCIDs(ucid)).thenReturn(customerAllUcids);
		when(persistenceService.getLatestActiveCustomer(Mockito.any(), Mockito.any())).thenReturn(customer);
		when(vendorProcessorFactory.getVendorProcessor(Mockito.any())).thenReturn(vendor);
	}	
	 
	
	@Test
	public void processGetUtilityOrdersByBCMCustomerSuccessTest() {
		setMockResponses();
		when(vendor.getAllUtilities(Mockito.anyLong(), Mockito.any())).thenReturn(responseModelArray);		
		vendorOrdersResponseModelList =  utilityService.getUtilityOrdersByBCMCustomer(customer);
		assertNotNull(vendorOrdersResponseModelList);
		
	}
	
	
}
