package com.progrexion.bcm.services.v1.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;

import java.util.HashSet;
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
import com.progrexion.bcm.common.model.v1.PaymentAccountRequestModel;
import com.progrexion.bcm.common.model.v1.PaymentAccountResponseModel;
import com.progrexion.bcm.common.model.v1.PlaidReconnectResponseModel;
import com.progrexion.bcm.common.model.v1.TransactionResponseModel;
import com.progrexion.bcm.common.model.vendor.VendorPaymentAccountRequestModel;
import com.progrexion.bcm.common.model.vendor.VendorPaymentAccountResponseModel;
import com.progrexion.bcm.common.model.vendor.VendorPlaidReconnectResponseModel;
import com.progrexion.bcm.common.processor.VendorAPINotification;
import com.progrexion.bcm.common.processor.VendorProcessor;
import com.progrexion.bcm.model.entities.BCMCustomer;
import com.progrexion.bcm.services.adapter.CustomerMasterAdapter;
import com.progrexion.bcm.services.builder.PlaidRequestBuilder;
import com.progrexion.bcm.services.constants.TestConstants;
import com.progrexion.bcm.services.factory.VendorProcessorFactory;
import com.progrexion.bcm.services.handler.VendorAPILogHandler;
import com.progrexion.bcm.services.persistence.PersistenceService;
import com.progrexion.bcm.services.util.TestServiceUtils;
import com.progrexion.bcm.services.v1.CustomerService;
import com.progrexion.bcm.services.v1.TransactionService;
import com.progrexion.bcm.services.validator.PlaidValidator;




@RunWith(MockitoJUnitRunner.class)
public class PlaidServiceImplTest {

	@InjectMocks
	private PlaidServiceImpl plaidService;
	@Mock
	private ModelMapper modelMapper;
	@Mock
	private VendorProcessorFactory vendorProcessorFactory;
	@Mock
	private VendorProcessor vendor;
	@Mock
	private PlaidValidator validator;
	@Mock
	private CustomerMasterAdapter customerMasterAdapter;
	@Mock
	private PersistenceService persistenceService;
	@Mock
	private PlaidRequestBuilder plaidRequestBuilder;
	@Mock
	private VendorAPILogHandler logHandler;
	@Mock
	private CustomerService customerService;
	@Mock
	private TransactionService transactionService;


	Set<Long> customerAllUcids = new HashSet<>();
	private BCMCustomer bCMCustomer;
    private PaymentAccountRequestModel reqModel;
    private PaymentAccountResponseModel resModel;
	private Long ucid;
	private String brand;
	TransactionResponseModel transactionCreateResponse;
	
    @Before
    public void setUp() {
    	MockitoAnnotations.initMocks(this);
       
        ucid = TestConstants.UCID;
        brand = TestConstants.BRAND_CCOM;
        reqModel =  TestServiceUtils.getPaymentAccountRequestModelObj();
        reqModel.setPublicToken("public-sandbox-34345");
        reqModel.setPlaidAccountId("asd3465");
        bCMCustomer = TestServiceUtils.getCustomerObject();
        transactionCreateResponse = TestServiceUtils.getPlaidPaymentAccountObj();
		when(transactionService.createOrUpdateTransactionFinder(Mockito.any(),Mockito.any())).thenReturn(transactionCreateResponse);

    }
	
	@Test
	public void test_processCreatePaymentAccountSuccessTest() {
		setMockResponses();
		VendorPaymentAccountResponseModel[] responseArray = new VendorPaymentAccountResponseModel[3];
		responseArray[0] = TestServiceUtils.getVendorPaymentAccountResponseModelObj();
		when(vendor.createPlaidPaymentAccount(Mockito.any(), Mockito.anyLong(), Mockito.any())).thenReturn(responseArray);
		resModel = plaidService.createPlaidPaymentAccount(ucid, brand, reqModel);
		assertNull(resModel);
	}	
	

	@Test
	public void test_processGetPaymentAccountSuccessTest() {
		setMockResponses();
		VendorPaymentAccountResponseModel responseModel = new VendorPaymentAccountResponseModel();
		responseModel.setId(101l);
		when(vendor.getPlaidPaymentAccountDetails(Mockito.anyLong(), Mockito.any())).thenReturn(responseModel);
		PaymentAccountResponseModel response = plaidService.getPaymentAccountDetails(ucid, brand);
		assertNull(response);
	}	
	
	
	@Test
	public void test_processGetPaymentAccountExceptionTest() {
		setMockResponses();
		when(vendor.getPlaidPaymentAccountDetails(Mockito.any(), Mockito.any())).thenReturn(null);
		
		try {
			plaidService.getPaymentAccountDetails(ucid, brand);
			// exception expected
		} catch (BCMModuleException e) {
			
		assertEquals(BCMModuleExceptionEnum.PLAID_CONNECTION_INVALID.getCode(), e.getErrorCode());
		}

	}
	private void setMockResponses()
	{
		 customerAllUcids.add(ucid);
		 when(customerMasterAdapter.getCustomerUCIDs(ucid)).thenReturn(customerAllUcids);
		when(persistenceService.getLatestActiveCustomer(Mockito.any(), Mockito.any())).thenReturn(bCMCustomer);
		when(vendorProcessorFactory.getVendorProcessor(Mockito.any())).thenReturn(vendor);
	}
	
	@Test
	public void test_reconnectPlaidSuccessTest() {
		setMockResponses();
		when(vendor.reconnectPlaid(Mockito.any(), Mockito.any(),Mockito.any(), Mockito.any())).thenReturn(new VendorPlaidReconnectResponseModel());		
		PlaidReconnectResponseModel response = plaidService.reconnectPlaid(ucid, brand,"PLAID_RECONNECTION");
		assertNull(response);
	}
	 	
	
}
