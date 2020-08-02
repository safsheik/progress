package com.progrexion.bcm.services.v1.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
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
import com.progrexion.bcm.common.model.v1.MatchTransactionRequestModel;
import com.progrexion.bcm.common.model.v1.MatchTransactionResponseModel;
import com.progrexion.bcm.common.model.v1.TransactionRequestModel;
import com.progrexion.bcm.common.model.v1.TransactionResponseModel;
import com.progrexion.bcm.common.model.vendor.VendorLeaseResponseModel;
import com.progrexion.bcm.common.model.vendor.VendorMatchTransactionResponseModel;
import com.progrexion.bcm.common.model.vendor.VendorPaymentAccountResponseModel;
import com.progrexion.bcm.common.model.vendor.VendorSearchTransactionResponseModel;
import com.progrexion.bcm.common.model.vendor.VendorTransactionResponseModel;
import com.progrexion.bcm.common.processor.VendorProcessor;
import com.progrexion.bcm.model.entities.BCMCustomer;
import com.progrexion.bcm.services.adapter.CustomerMasterAdapter;
import com.progrexion.bcm.services.builder.TransactionRequestBuilder;
import com.progrexion.bcm.services.constants.TestConstants;
import com.progrexion.bcm.services.factory.VendorProcessorFactory;
import com.progrexion.bcm.services.handler.VendorAPILogHandler;
import com.progrexion.bcm.services.persistence.PersistenceService;
import com.progrexion.bcm.services.util.TestServiceUtils;
import com.progrexion.bcm.services.v1.CustomerService;
import com.progrexion.bcm.services.validator.TransactionValidator;




@RunWith(MockitoJUnitRunner.class)
public class TransactionServiceImplTest {

	@InjectMocks
	private TransactionServiceImpl transactionService;
	@Mock
	private ModelMapper modelMapper;
	@Mock
	private VendorProcessorFactory vendorProcessorFactory;
	@Mock
	private VendorProcessor vendor;
	@Mock
	private TransactionValidator validator;
	@Mock
	private CustomerMasterAdapter customerMasterAdapter;
	@Mock
	private PersistenceService persistenceService;
	@Mock
	private TransactionRequestBuilder transactionRequestBuilder;
	@Mock
	private VendorAPILogHandler logHandler;
	@Mock
	private CustomerService customerService;

	Set<Long> customerAllUcids = new HashSet<>();
	private BCMCustomer bcmCustomer;
    private TransactionRequestModel reqModel;
    private TransactionResponseModel resModel;
    private boolean isUpdate;
    private MatchTransactionRequestModel matchReqModel;
    private MatchTransactionResponseModel matchResModel;
    private VendorLeaseResponseModel leaseResponseModel;
    private VendorPaymentAccountResponseModel paymentAccountResponseModel;
    private VendorTransactionResponseModel vendorTransactionResponseModel;
	private Long ucid;
	private String brand;
    @Before
    public void setUp() throws Exception {

    	MockitoAnnotations.initMocks(this);
        ucid = TestConstants.UCID;
        brand = TestConstants.BRAND_CCOM;
        reqModel =  TestServiceUtils.getTransactionRequestModelObj();
        bcmCustomer = TestServiceUtils.getCustomerObject();
        leaseResponseModel = TestServiceUtils.getVendorLeaseResponseModelObject();
        paymentAccountResponseModel=TestServiceUtils.getVendorPaymentAccountResponseModelObj();
        vendorTransactionResponseModel =TestServiceUtils.getVendorTransactionResponseModelObj();
        isUpdate=true;
    }
	
	@Test
	public void test_processCreateTransactionSuccessTest(){
		setMockResponses();
		VendorSearchTransactionResponseModel[] responseArray = new VendorSearchTransactionResponseModel[1];
		responseArray[0] = TestServiceUtils.getVendorSearchTransactionResponseModelObj();
		when(vendor.getLeaseInfo(Mockito.any(), Mockito.any())).thenReturn(leaseResponseModel);
		when(vendor.getPlaidPaymentAccountDetails(Mockito.any(), Mockito.any())).thenReturn(paymentAccountResponseModel);
		when(vendor.createTransactionFinder(Mockito.any(), Mockito.any(),Mockito.any(), Mockito.any())).thenReturn(vendorTransactionResponseModel);
		when(vendor.searchByTransactionFinder(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(responseArray);
		
		when(customerService.updateBcmCustomer(Mockito.any())).thenReturn(bcmCustomer);
		resModel = transactionService.createTransactionFinder(ucid, brand,
				reqModel);
		assertNotNull(resModel);
	}	
	
	@Test
	public void test_processCreateTransactionSuccessTestMultiple(){
		setMockResponses();
		VendorSearchTransactionResponseModel[] responseArray = new VendorSearchTransactionResponseModel[2];
		responseArray[0] = TestServiceUtils.getVendorSearchTransactionResponseModelObj();
		responseArray[1] = TestServiceUtils.getVendorSearchTransactionResponseModelObj();
		when(vendor.getLeaseInfo(Mockito.any(), Mockito.any())).thenReturn(leaseResponseModel);
		when(vendor.getPlaidPaymentAccountDetails(Mockito.any(), Mockito.any())).thenReturn(paymentAccountResponseModel);
		when(vendor.createTransactionFinder(Mockito.any(), Mockito.any(),Mockito.any(), Mockito.any())).thenReturn(vendorTransactionResponseModel);
		when(vendor.searchByTransactionFinder(Mockito.any(),Mockito.any(), Mockito.any())).thenReturn(responseArray);
		
		when(customerService.updateBcmCustomer(Mockito.any())).thenReturn(bcmCustomer);
		resModel = transactionService.createTransactionFinder(ucid, brand,
				reqModel);
		assertNotNull(resModel);
	}	
	
	@Test
	public void test_processSearchByTransactinFinderSuccessTest(){
		setMockResponses();
		VendorSearchTransactionResponseModel[] responseArray = new VendorSearchTransactionResponseModel[1];
		responseArray[0] = TestServiceUtils.getVendorSearchTransactionResponseModelObj();
		when(vendor.getLeaseInfo(Mockito.any(), Mockito.any())).thenReturn(leaseResponseModel);
		when(vendor.getPlaidPaymentAccountDetails(Mockito.any(), Mockito.any())).thenReturn(paymentAccountResponseModel);
		when(vendor.searchByTransactionFinder(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(responseArray);
		responseArray = transactionService.searchByTransactinFinder(ucid, brand);
		assertNotNull(responseArray);
	}	
	@Test
	public void test_processMatchTransactionSuccessTest(){
		setMockResponses();
		VendorMatchTransactionResponseModel[] responseArray = new VendorMatchTransactionResponseModel[1];
		responseArray[0] = TestServiceUtils.getVendorMatchTransactionResponseModelObj();
	
		when(vendor.matchTransaction(Mockito.any(), Mockito.any(),Mockito.any(), Mockito.any())).thenReturn(responseArray);

		matchResModel = transactionService.matchTransaction(ucid, brand,
				matchReqModel);
		assertNotNull(matchResModel);
	}	
	
	@Test
	public void test_processMatchTransactionExceptionTest(){
		setMockResponses();
		VendorMatchTransactionResponseModel[] responseArray = new VendorMatchTransactionResponseModel[1];
		responseArray[0] = TestServiceUtils.getVendorMatchTransactionResponseModelObj();
		bcmCustomer.setTransactionFinderId(null);
		when(persistenceService.getLatestActiveCustomer(Mockito.any(), Mockito.any())).thenReturn(bcmCustomer);

		try{
			transactionService.matchTransaction(ucid, brand,matchReqModel);
		}
		catch (BCMModuleException e) {
	
			assertEquals(BCMModuleExceptionEnum.MATCH_TRX_FINDER_NO_RECORDS.getCode(), e.getErrorCode());
		}
	}	
	@Test
	public void test_processSearchTransactionDetailsSuccessTest(){
		setMockResponses();
		VendorSearchTransactionResponseModel[] responseArray = new VendorSearchTransactionResponseModel[1];
		responseArray[0] = TestServiceUtils.getVendorSearchTransactionResponseModelObj();
		when(vendor.searchTransactionByFinder(Mockito.any(),Mockito.any(), Mockito.any())).thenReturn(responseArray);
		resModel = transactionService.searchTransactionDetails(ucid, brand);
		assertNotNull(resModel);
	}	
	private void setMockResponses()
	{
		 customerAllUcids.add(ucid);
		 when(customerMasterAdapter.getCustomerUCIDs(ucid)).thenReturn(customerAllUcids);
		when(persistenceService.getLatestActiveCustomer(Mockito.any(), Mockito.any())).thenReturn(bcmCustomer);
		when(vendorProcessorFactory.getVendorProcessor(Mockito.any())).thenReturn(vendor);
	}
		
	@Test
	public void test_processCreateOrUpdateTransactionFinderSuccessTest(){
		setMockResponses();
		VendorSearchTransactionResponseModel[] responseArray = new VendorSearchTransactionResponseModel[1];
		responseArray[0] = TestServiceUtils.getVendorSearchTransactionResponseModelObj();
		when(vendor.getLeaseInfo(Mockito.any(), Mockito.any())).thenReturn(leaseResponseModel);
		when(vendor.updateTransactionFinder(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(isUpdate);
		when(vendor.searchByTransactionFinder(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(responseArray);
		resModel=transactionService.createOrUpdateTransactionFinder(bcmCustomer, reqModel);
		assertNotNull(resModel);
		
	}
	 
}
