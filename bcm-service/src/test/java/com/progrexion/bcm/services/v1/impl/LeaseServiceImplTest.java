package com.progrexion.bcm.services.v1.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
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
import com.progrexion.bcm.common.model.v1.LeaseRequestModel;
import com.progrexion.bcm.common.model.v1.LeaseResponseModel;
import com.progrexion.bcm.common.model.vendor.VendorLeaseResponseModel;
import com.progrexion.bcm.common.model.vendor.VendorOrdersResponseModel;
import com.progrexion.bcm.common.processor.VendorProcessor;
import com.progrexion.bcm.model.entities.BCMCustomer;
import com.progrexion.bcm.services.adapter.CustomerMasterAdapter;
import com.progrexion.bcm.services.builder.LeaseRequestBuilder;
import com.progrexion.bcm.services.constants.TestConstants;
import com.progrexion.bcm.services.factory.VendorProcessorFactory;
import com.progrexion.bcm.services.persistence.PersistenceService;
import com.progrexion.bcm.services.util.TestServiceUtils;
import com.progrexion.bcm.services.validator.LeaseValidator;




@RunWith(MockitoJUnitRunner.class)
public class LeaseServiceImplTest {
	@InjectMocks
	private LeaseServiceImpl leaseService;
	@Mock
	private ModelMapper modelMapper;
	@Mock
	private VendorProcessorFactory vendorProcessorFactory;
	@Mock
	private VendorProcessor vendor;
	@Mock
	private LeaseValidator validator;
	@Mock
	private CustomerMasterAdapter customerMasterAdapter;
	@Mock
	private PersistenceService persistenceService;
	@Mock
	private LeaseRequestBuilder leaseRequestBuilder;
	Set<Long> customerAllUcids = new HashSet<>();
	private LeaseRequestModel leaseReqModel;
	private BCMCustomer bCMCustomer;
	private boolean isUserCreated;
	private Long ucid;
	private String brand;
	private LeaseRequestModel leaseRequestModel;
	private LeaseResponseModel leaseResponseModel;
	private VendorLeaseResponseModel vendorLeaseResponseModel;
  
  
    @Before
    public void setUp() {

    	MockitoAnnotations.initMocks(this);
        ucid = TestConstants.UCID;
        brand = TestConstants.BRAND_CCOM;
        vendorLeaseResponseModel= TestServiceUtils.getVendorLeaseResponseModelObject();
        leaseResponseModel = TestServiceUtils.getLeaseResponseModelObject();
        bCMCustomer = TestServiceUtils.getCustomerObject();

    }
  
  @Test
  public void test_createLeaseSuccessTest() {
	 customerAllUcids.add(ucid);
	when(vendorProcessorFactory.getVendorProcessor(Mockito.any())).thenReturn(vendor);
	when(vendor.createLease(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(vendorLeaseResponseModel);

	isUserCreated = leaseService.createLease(bCMCustomer, leaseReqModel);
	assertEquals(isUserCreated,true);

  }
  
  @Test
  public void test_createLeaseAlreadyAvailableExceptionTest(){
	  customerAllUcids.add(ucid);
		when(vendorProcessorFactory.getVendorProcessor(Mockito.any())).thenReturn(vendor);
		when(vendor.getLeaseInfo(Mockito.anyLong(),Mockito.any())).thenReturn(vendorLeaseResponseModel);
		
    
    try {
      leaseService.createLease(bCMCustomer, leaseReqModel);
      // exception expected
    } catch (BCMModuleException e) {
      
    assertEquals(BCMModuleExceptionEnum.LEASE_ALREADY_AVAILABLE.getCode(), e.getErrorCode());
    }
}
  
  @Test
  public void test_getLeaseSuccessTest(){
	 customerAllUcids.add(ucid);
	 when(customerMasterAdapter.getCustomerUCIDs(ucid)).thenReturn(customerAllUcids);
	when(persistenceService.getLatestActiveCustomer(Mockito.any(), Mockito.any())).thenReturn(bCMCustomer);
	when(vendorProcessorFactory.getVendorProcessor(Mockito.any())).thenReturn(vendor);
	when(vendor.getLeaseInfo(Mockito.any(), Mockito.any())).thenReturn(vendorLeaseResponseModel);
	leaseResponseModel = leaseService.getLeaseInfo(ucid, brand);
	 assertNotNull(leaseResponseModel);
  } 
  
  @Test
  public void test_getLeaseExceptionTest(){
	 customerAllUcids.add(ucid);
	 when(customerMasterAdapter.getCustomerUCIDs(ucid)).thenReturn(customerAllUcids);
	when(persistenceService.getLatestActiveCustomer(Mockito.any(), Mockito.any())).thenReturn(bCMCustomer);
	when(vendorProcessorFactory.getVendorProcessor(Mockito.any())).thenReturn(vendor);
	when(vendor.getLeaseInfo(Mockito.any(), Mockito.any())).thenReturn(null);
	try
	{
		leaseService.getLeaseInfo(ucid, brand);
	}
	catch(BCMModuleException e)
	{
		 assertEquals(BCMModuleExceptionEnum.LEASE_NOT_FOUND.getCode(), e.getErrorCode());
	}

  } 
  
  @Test
  public void test_getLeaseOrdersSuccessTest(){
	 customerAllUcids.add(ucid);
	 when(customerMasterAdapter.getCustomerUCIDs(ucid)).thenReturn(customerAllUcids);
	when(persistenceService.getLatestActiveCustomer(Mockito.any(), Mockito.any())).thenReturn(bCMCustomer);
	when(vendorProcessorFactory.getVendorProcessor(Mockito.any())).thenReturn(vendor);
	when(vendor.getLeaseInfo(Mockito.any(), Mockito.any())).thenReturn(vendorLeaseResponseModel);
	when(vendor.getLeaseOrders(Mockito.any(),Mockito.any(), Mockito.any())).thenReturn(new VendorOrdersResponseModel[2]);
	assertNotNull(leaseService.getLeaseOrders(ucid, brand));
  } 
  
  @Test
  public void test_getLeaseOrdersExceptionTest1(){
	 customerAllUcids.add(ucid);
	 when(customerMasterAdapter.getCustomerUCIDs(ucid)).thenReturn(customerAllUcids);
	when(persistenceService.getLatestActiveCustomer(Mockito.any(), Mockito.any())).thenReturn(bCMCustomer);
	when(vendorProcessorFactory.getVendorProcessor(Mockito.any())).thenReturn(vendor);
	when(vendor.getLeaseInfo(Mockito.any(), Mockito.any())).thenReturn(null);
	try
	{
		leaseService.getLeaseOrders(ucid, brand);
	}
	catch(BCMModuleException e)
	{
		 assertEquals(BCMModuleExceptionEnum.LEASE_NOT_FOUND.getCode(), e.getErrorCode());
	}
  } 
  
  @Test
  public void test_getLeaseOrdersExceptionTest2(){
	 customerAllUcids.add(ucid);
	 when(customerMasterAdapter.getCustomerUCIDs(ucid)).thenReturn(customerAllUcids);
	when(persistenceService.getLatestActiveCustomer(Mockito.any(), Mockito.any())).thenReturn(bCMCustomer);
	when(vendorProcessorFactory.getVendorProcessor(Mockito.any())).thenReturn(vendor);
	when(vendor.getLeaseInfo(Mockito.any(), Mockito.any())).thenReturn(vendorLeaseResponseModel);
	try
	{
		leaseService.getLeaseOrders(ucid, brand);
	}
	catch(BCMModuleException e)
	{
		 assertEquals(BCMModuleExceptionEnum.LEASE_NOT_FOUND.getCode(), e.getErrorCode());
	}
  } 
  
  @Test
  public void test_getLeaseOrdersExceptionTest3(){
	 customerAllUcids.add(ucid);
	 when(customerMasterAdapter.getCustomerUCIDs(ucid)).thenReturn(customerAllUcids);
	when(persistenceService.getLatestActiveCustomer(Mockito.any(), Mockito.any())).thenReturn(bCMCustomer);
	when(vendorProcessorFactory.getVendorProcessor(Mockito.any())).thenReturn(vendor);
	when(vendor.getLeaseInfo(Mockito.any(), Mockito.any())).thenReturn(vendorLeaseResponseModel);
	when(vendor.getLeaseInfo(Mockito.any(), Mockito.any())).thenReturn(vendorLeaseResponseModel);
	when(vendor.getLeaseOrders(Mockito.any(),Mockito.any(), Mockito.any())).thenReturn(null);
	try
	{
		leaseService.getLeaseOrders(ucid, brand);
	}
	catch(BCMModuleException e)
	{
		 assertEquals(BCMModuleExceptionEnum.LEASE_NOT_FOUND.getCode(), e.getErrorCode());
	}
  } 

	@Test
	public void test_getLeaseInfoSuccessTest() {
		customerAllUcids.add(ucid);
		when(vendorProcessorFactory.getVendorProcessor(Mockito.any())).thenReturn(vendor);
		when(vendor.getLeaseInfo(Mockito.anyLong(), Mockito.any())).thenReturn(vendorLeaseResponseModel);
		assertTrue(leaseService.getLeaseInfo(bCMCustomer));
	}

	@Test
	public void test_createOrUpdateLeaseExceptionTest() {
		customerAllUcids.add(ucid);
		try {
			leaseService.createOrUpdateLease(ucid, brand, leaseRequestModel, true);
		} catch (BCMModuleException e) {
			assertEquals(BCMModuleExceptionEnum.CUSTOMER_INFO_NOT_FOUND.getCode(), e.getErrorCode());
		}
	} 
  
}