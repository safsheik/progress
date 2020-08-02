package com.progrexion.bcm.services.v1.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

import java.util.Optional;

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
import com.progrexion.bcm.common.model.v1.CreateUserRequestModel;
import com.progrexion.bcm.common.model.v1.SubscriptionResponseModel;
import com.progrexion.bcm.common.model.vendor.VendorCreateUserResponseModel;
import com.progrexion.bcm.common.model.vendor.VendorSubscriptionResponseModel;
import com.progrexion.bcm.common.model.vendor.VendorTokenResponseModel;
import com.progrexion.bcm.common.processor.VendorAPINotification;
import com.progrexion.bcm.common.processor.VendorProcessor;
import com.progrexion.bcm.model.entities.BCMCustomer;
import com.progrexion.bcm.model.entities.CustomerSubscription;
import com.progrexion.bcm.model.repositories.CustomerRepository;
import com.progrexion.bcm.model.repositories.CustomerSubscriptionRepository;
import com.progrexion.bcm.renttrack.client.RentTrackClient;
import com.progrexion.bcm.renttrack.client.services.AccessTokenProviderService;
import com.progrexion.bcm.renttrack.serviceimpl.RentTrackProcessor;
import com.progrexion.bcm.services.adapter.CustomerMasterAdapter;
import com.progrexion.bcm.services.builder.EntityDataBuilder;
import com.progrexion.bcm.services.factory.VendorProcessorFactory;
import com.progrexion.bcm.services.handler.VendorAPILogHandler;
import com.progrexion.bcm.services.handler.VendorAPINotificationHandler;
import com.progrexion.bcm.services.util.TestServiceUtils;
import com.progrexion.bcm.services.v1.LeaseService;
import com.progrexion.bcm.services.v1.SubscriptionService;
import com.progrexion.bcm.services.validator.BCMValidator;
import com.progrexion.bcm.services.validator.LeaseValidator;

@RunWith(MockitoJUnitRunner.class)
public class CustomerServiceImplTest {

	@InjectMocks
	private CustomerServiceImpl customerService;
	
	private CreateUserRequestModel reqModel;
	
	@Mock
	private ModelMapper modelMapper;

	@Mock
	private VendorProcessorFactory vendorProcessorFactory;
	private boolean isLeaseCreated;
	private Long ucid;
	private String brand;
	private BCMCustomer customer;
	@Mock
	private VendorProcessor vendor ;

	@Mock
	private RentTrackProcessor rentTrackProcessor;
	
	private VendorCreateUserResponseModel vendorCreateUserResponseModel;
	@Mock
	private CustomerRepository customerRepository;
	@Mock
	private VendorAPILogHandler logHandler;
	@Mock
	private BCMValidator bCMValidator;
	
	private Optional<BCMCustomer> customerOpt;
	private VendorTokenResponseModel tokenModel;
	@Mock
	private AccessTokenProviderService rtTokenProvider;
	@Mock
	private SubscriptionService subscriptionService;
	
	@Mock
	private LeaseService leaseService;
	
	private VendorSubscriptionResponseModel vendorSubscriptionModel;
	
	@Mock
	private LeaseValidator leaseValidator;
	@Mock
	private VendorAPINotificationHandler vendorAPINotificationHandler;
	@Mock
	private VendorProcessor vendorProcessor;
	@Mock
	private RentTrackClient rentTrackClient;
	@Mock
	private EntityDataBuilder entityBuilder;
	@Mock
	private VendorAPINotification vendorAPINotification;
	@Mock
	private CustomerMasterAdapter customerMasterAdapter;
	
	private SubscriptionResponseModel[] subscriptionResponseModel;
	
	private CustomerSubscription customerSubscription ;
	@Mock
	private CustomerSubscriptionRepository customerSubscriptionRepository;

	@Before
	public void setUp() throws Exception {

		MockitoAnnotations.initMocks(this);

		customer = TestServiceUtils.getCustomerObject();
		reqModel = TestServiceUtils.getCreateUserObject();
		customerOpt = Optional.of(customer);
		
		ucid = (long) 101;
		brand = "CCOM";
		isLeaseCreated = true;
		
		reqModel.setLeaseData(TestServiceUtils.getLeaseObject());

		vendorCreateUserResponseModel = new VendorCreateUserResponseModel();
		vendorCreateUserResponseModel.setId(102l);

		when(customerRepository.findById(Mockito.anyLong())).thenReturn(customerOpt);

		tokenModel = new VendorTokenResponseModel();
		tokenModel.setAccessToken("ASDDD543693654GHGHJMH");
		tokenModel.setExpiresIn(3l);
		tokenModel.setRefreshToken("GHKKASDDD543693654GHGHJMH");
		tokenModel.setTokenType("access_token");

		vendorSubscriptionModel = new VendorSubscriptionResponseModel();
		vendorSubscriptionModel.setSubscriptionId(102l);
		
		SubscriptionResponseModel subcriptionResponse = new SubscriptionResponseModel();
		subcriptionResponse.setSubscriptionId(102l);
		
		subscriptionResponseModel = new SubscriptionResponseModel[3];
		subscriptionResponseModel[0]=subcriptionResponse;
		
		
		customerSubscription = new CustomerSubscription();
		customerSubscription.setSubscriptionId(102l);
		
		
		when(leaseService.createLease( Mockito.any(), Mockito.any())).thenReturn(isLeaseCreated);
		


		when(vendorProcessorFactory.getVendorProcessor(Mockito.any())).thenReturn(vendor);
		when(entityBuilder.buildCustomerEntityForNewUser(Mockito.any(), Mockito.any(), Mockito.any()))
				.thenReturn(customer);

	}

	@Test
	public void test_processCreateUserAccountSuccessTest() {
		when(vendor.createUserAccount(Mockito.any(), Mockito.any())).thenReturn(vendorCreateUserResponseModel);
		when(vendor.updateTenantUserDetails(Mockito.anyLong(), Mockito.any(), Mockito.any())).thenReturn(true);
		when(leaseService.createLease(Mockito.any(), Mockito.any())).thenReturn(isLeaseCreated);
		when(leaseValidator.getCustomerRTSubscription(Mockito.any())).thenReturn(subscriptionResponseModel);
		when(customerMasterAdapter.getCustomerParentUCID(ucid)).thenReturn(ucid);
		when(entityBuilder.buildCustomerEntityForNewUser(Mockito.any(), Mockito.any(), Mockito.any()))
				.thenReturn(customer);
		when(customerRepository.saveAndFlush(Mockito.any())).thenReturn(customer);
		when(customerRepository.save(Mockito.any())).thenReturn(customer);
		when(customerSubscriptionRepository.findFirstByCustomerOrderByCreatedDateDesc(Mockito.any()))
				.thenReturn(customerSubscription);
		assertNotNull(customerService.createUserAccount(ucid, brand, reqModel));

	}

	@Test
	public void test_processCreateUserAccountExceptionTest() {
		when(customerMasterAdapter.getCustomerParentUCID(ucid)).thenReturn(ucid);
		when(customerRepository.findByCustEmail(Mockito.any())).thenReturn(customerOpt);
		when(leaseValidator.getCustomerRTSubscription(Mockito.any())).thenReturn(subscriptionResponseModel);

		try {
			customerService.createUserAccount(ucid,brand,reqModel);
			// exception expected
		} catch (BCMModuleException e) {
			
		assertEquals(BCMModuleExceptionEnum.TENANT_USER_NOT_CREATED.getCode(), e.getErrorCode());
		}

	}
	
	@Test
	public void test_processCreateCustomerSuccessTest() {
		when(customerRepository.save(Mockito.any())).thenReturn(customer);
		assertNotNull(customerService.createBcmCustomer(customer));
	}
	
	@Test
	public void test_processUpdateCustomerSuccessTest() {
		when(customerRepository.saveAndFlush(Mockito.any())).thenReturn(customer);
		assertNotNull(customerService.updateBcmCustomer(customer));
	}
	@Test
	public void test_processUpdateCustomerExceptionTest() {
		when(customerRepository.saveAndFlush(Mockito.any())).thenReturn(null);
		try
		{
			customerService.updateBcmCustomer(customer);
		}
		catch (BCMModuleException e) {
			
			assertEquals(BCMModuleExceptionEnum.CUSTOMER_INFO_ALREADY_EXISTS.getCode(), e.getErrorCode());
			}
	}

	@Test
	public void test_processGetCustomerByIdSuccessTest() {
		
		assertNotNull(customerService.getCustomerById(1l));
	}
	
	@Test
	public void test_processGetCustomerByEmailOrUcIdSuccessTest() {
		when(customerRepository.findByCustEmail(Mockito.any())).thenReturn(customerOpt);
		assertNotNull(customerService.getCustomerByEmail(reqModel.getEmail()));
	}
	
	@Test
	public void test_processGetCustomerByUcIdAndBrandSuccessTest() {
		when(customerRepository.findByUcIdAndBrand(Mockito.anyLong(), Mockito.any())).thenReturn(customerOpt);
		assertNotNull(customerService.getCustomerByUcIdAndBrand(ucid,brand));
	}
	
	@Test
	public void test_processGetTransactionFinderIdSuccessTest() {
		when(customerRepository.findByUcIdAndBrand(Mockito.anyLong(), Mockito.any())).thenReturn(customerOpt);
		assertNotNull(customerService.getTransactionFinderId(ucid,brand));
	}
	
	@Test
	public void test_processGetCustomerByUcIdSuccessTest() {
		when(customerRepository.findByUcId(Mockito.anyLong())).thenReturn(customerOpt);
		assertNotNull(customerService.getCustomerByUcId(ucid));
	}

}
