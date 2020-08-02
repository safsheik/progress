package com.progrexion.bcm.services.builder;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.progrexion.bcm.common.enums.OrderTypeEnum;
import com.progrexion.bcm.common.enums.SubscriptionStatusEnum;
import com.progrexion.bcm.common.jobs.enums.OrderProcessStatusEnum;
import com.progrexion.bcm.common.model.v1.CreateUserRequestModel;
import com.progrexion.bcm.common.model.vendor.VendorTokenResponseModel;
import com.progrexion.bcm.model.entities.BCMCustomer;
import com.progrexion.bcm.model.entities.CustomerActivity;
import com.progrexion.bcm.model.entities.CustomerOrderProcess;
import com.progrexion.bcm.model.entities.CustomerSubscription;
import com.progrexion.bcm.model.entities.ExternalLog;
import com.progrexion.bcm.model.entities.FailedDeprovision;
import com.progrexion.bcm.model.entities.FailedTransactionPull;
import com.progrexion.bcm.model.entities.Order;
import com.progrexion.bcm.model.entities.WebServiceLog;
import com.progrexion.bcm.model.repositories.OrderRepository;
import com.progrexion.bcm.services.constants.TestConstants;
import com.progrexion.bcm.services.util.DataBuilderUtil;
import com.progrexion.bcm.services.util.TestServiceUtils;

@RunWith(MockitoJUnitRunner.class)
public class EntityDataBuilderTest {

	@Mock
	private ObjectMapper mockObjectMapper;
	@Mock
	private OrderRepository orderRepo; 
	@InjectMocks
	private EntityDataBuilder entityDataBuilder;
	
	private BCMCustomer customer;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
	}
	@Test
	public void test_getWebServiceLogsEntity() {
		WebServiceLog response = entityDataBuilder.getWebServiceLogsEntity(DataBuilderUtil.buildWebServiceLogsModel());
		assertNotNull(response);
	}


	@Test
	public void test_buildExternalLogs() {
		customer = new BCMCustomer();
		customer.setBrand("CCOM");
		customer.setUcId((long)101);
		ExternalLog responseData = entityDataBuilder.buildExternalLogs(customer,(long)101, "/test/", null,
				TestConstants.MESSAGE_BODY,"POST");
		assertNotNull(responseData);
	}
	
	@Test
	public void test_getCustomerEntity() {
		CreateUserRequestModel requestModel = new CreateUserRequestModel();
		requestModel.setEmail("abc@gmail.com");
		VendorTokenResponseModel tokens=new VendorTokenResponseModel();
		tokens.setAccessToken("sdf342345456");
		tokens.setRefreshToken("sdf342345456");
		tokens.setExpiresIn(150l);
		customer =TestServiceUtils.getCustomerObject();
		BCMCustomer responseData = entityDataBuilder.getCustomerEntityForNewUserAccountFlow(customer, tokens);
		assertNotNull(responseData);	
	}

	@Test
	public void test_buildCustomerActivity() {
		customer = new BCMCustomer();
		customer.setBrand("CCOM");
		customer.setUcId((long)101);
		CustomerActivity responseData = entityDataBuilder.buildCustomerActivity(customer,"CREATE_TENANT");
		assertNotNull(responseData);
	}
	
	@Test
	public void test_buildCustomerActivityWithNull() {
		customer = new BCMCustomer();
		customer.setBrand("CCOM");
		customer.setUcId((long)101);
		CustomerActivity responseData = entityDataBuilder.buildCustomerActivity(customer,"CREATE_TENANT_TEST");
		assertNull(responseData);
	}
	@Test
	public void test_buildCustomerEntityForNewUser() {
		BCMCustomer responseData = entityDataBuilder.buildCustomerEntityForNewUser(101l,"CCOM","test@gmail.com");
		assertNotNull(responseData);
	}
	
	@Test
	public void test_buildCustomerSubscription() {
		CustomerSubscription responseData = entityDataBuilder.buildCustomerSubscription(TestConstants.getCustomerObject(),
				101l, SubscriptionStatusEnum.ACTIVE);
		assertNotNull(responseData);
	}
	
	@Test
	public void test_buildFailedTransactionPull() {
		FailedTransactionPull responseData = entityDataBuilder.buildFailedTransactionPull(TestConstants.getCustomerObject(),
				TestConstants.getNewJobObj(),"Error");
		assertNotNull(responseData);
	}
	
	@Test
	public void test_buildCustomerSubscriptionFailedDeprovision() {
		FailedDeprovision responseData = entityDataBuilder.buildCustomerSubscriptionFailedDeprovision(TestConstants.getCustomerObject(),
				"error","Error");
		assertNotNull(responseData);
	}
	
	@Test
	public void test_buildOrderEntity() {
		Order responseData = entityDataBuilder.buildOrderEntity(TestConstants.getVendorOrdersResponseModelObj(),
				TestConstants.getCustomerObject(),OrderTypeEnum.LEASE);
		assertNotNull(responseData);
	}
	
	@Test
	public void test_buildOrderEntity1() {
		Order bcmOrder = TestConstants.getOrderObj();
		when(orderRepo.findByRtOrderId(Mockito.any())).thenReturn(bcmOrder);
		Order responseData = entityDataBuilder.buildOrderEntity(TestConstants.getVendorOrdersResponseModelObj(),
				TestConstants.getCustomerObject(),OrderTypeEnum.LEASE);
		assertNotNull(responseData);
	}
	
	@Test
	public void test_buildOrderEntityNull() {
		when(orderRepo.findByRtOrderId(Mockito.any())).thenReturn(new Order());
		Order responseData = entityDataBuilder.buildOrderEntity(TestConstants.getVendorOrdersResponseModelObj(),
				TestConstants.getCustomerObject(),OrderTypeEnum.LEASE);
		assertNotNull(responseData);
	}
	
	@Test
	public void test_buildCustomerOrderProcessEntity() {
		assertNotNull(entityDataBuilder.buildCustomerOrderProcessEntity(TestConstants.getCustomerObject(),
				TestConstants.getNewJobObj(),OrderProcessStatusEnum.COMPLETED));
	}
	
	@Test
	public void test_buildProcessOrderMessageDto() {
		CustomerOrderProcess customerOrderProcess = new CustomerOrderProcess();
		customerOrderProcess.setJob(TestConstants.getNewJobObj());
		customerOrderProcess.setCustomer(TestConstants.getCustomerObject());
		assertNotNull(entityDataBuilder.buildProcessOrderMessageDto(customerOrderProcess));
	}

}
