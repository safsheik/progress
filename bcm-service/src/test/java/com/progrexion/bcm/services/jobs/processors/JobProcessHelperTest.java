package com.progrexion.bcm.services.jobs.processors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import com.progrexion.bcm.common.exceptions.BCMModuleException;
import com.progrexion.bcm.common.exceptions.BCMModuleExceptionEnum;
import com.progrexion.bcm.common.jobs.enums.JobNameEnum;
import com.progrexion.bcm.common.jobs.enums.OrderProcessStatusEnum;
import com.progrexion.bcm.common.model.vendor.VendorOrdersResponseModel;
import com.progrexion.bcm.common.properties.BCMConfigProperties;
import com.progrexion.bcm.jobs.v1.ProcessOrderMessageDTO;
import com.progrexion.bcm.model.entities.BCMCustomer;
import com.progrexion.bcm.model.entities.CustomerOrderProcess;
import com.progrexion.bcm.model.entities.FailedTransactionPull;
import com.progrexion.bcm.model.entities.Job;
import com.progrexion.bcm.model.entities.Order;
import com.progrexion.bcm.model.repositories.CustomerOrderProcessRepository;
import com.progrexion.bcm.model.repositories.FailedTransactionPullRepository;
import com.progrexion.bcm.model.repositories.OrderRepository;
import com.progrexion.bcm.services.builder.EntityDataBuilder;
import com.progrexion.bcm.services.constants.TestConstants;
import com.progrexion.bcm.services.jobs.publisher.CustomerProcessOrderMessagePublisher;
import com.progrexion.bcm.services.persistence.PersistenceService;
import com.progrexion.bcm.services.v1.CustomerService;
import com.progrexion.bcm.services.v1.LeaseService;
import com.progrexion.bcm.services.v1.UtilityService;




@RunWith(MockitoJUnitRunner.class)
public class JobProcessHelperTest {

	@InjectMocks
	private JobProcessHelper jobProcessHelper;
	@Mock
	private PersistenceService persistenceService;
	@Mock
	private LeaseService leaseService;
	@Mock
	private UtilityService utilityService;
	@Mock
	private CustomerService customerService;
	@Mock
	private EntityDataBuilder entityBuilder;
	@Mock
	private OrderRepository orderRepo;
	@Mock
	private FailedTransactionPullRepository failedTransactionPullRepo;
	@Mock
	private BCMConfigProperties bcmProperty;
	
	@Mock
	private CustomerOrderProcessRepository customerOrderProcessRepo;
	@Mock
	private CustomerProcessOrderMessagePublisher messagePublisher;
	
	@Before
	public void setUp() throws Exception {

		MockitoAnnotations.initMocks(this);
		when(bcmProperty.getTransactionPullCycleInDays()).thenReturn(14);
	}
	
	@Test
	public void test_getAllCustomersToGetOrders() {
		List<BCMCustomer> custList = new ArrayList<>();
		when(persistenceService.getNewAndReportExpiredCustomers(Mockito.any(),Mockito.any())).thenReturn(custList);
		List<BCMCustomer> resCustList = jobProcessHelper.getAllCustomersToGetOrders(ZonedDateTime.now(), TestConstants.BRAND_CCOM);
		assertEquals(custList, resCustList);
	}
	
	@Test
	public void test_getNumberOfDaystoCheck() {
		int days = jobProcessHelper.getNumberOfDaystoCheck(JobNameEnum.RENTTRACK_REPORTED_ORDERS);
		assertNotNull(days);
	}
	
	@Test
	public void test_processOrderReport() {
		List<VendorOrdersResponseModel> orderList = new ArrayList<>();
		VendorOrdersResponseModel orderReponse = TestConstants.getVendorOrdersResponseModelObj();
		orderList.add(orderReponse);
		Order order = new Order();
		order.setRtOrderId(Long.valueOf(orderReponse.getOrderId()));
		when(leaseService.getLeaseOrdersByBCMCustomer(Mockito.any())).thenReturn(orderList);
		when(utilityService.getUtilityOrdersByBCMCustomer(Mockito.any())).thenReturn(orderList);
		when(orderRepo.saveAll(Mockito.any())).thenReturn(null);	
		jobProcessHelper.processOrderReport(new BCMCustomer());
		assertNotNull(jobProcessHelper);
	}
	
	@Test
	public void test_processOrderReportBCMException() {
		List<VendorOrdersResponseModel> orderList = new ArrayList<>();
		VendorOrdersResponseModel orderReponse = TestConstants.getVendorOrdersResponseModelObj();
		orderList.add(orderReponse);
		when(leaseService.getLeaseOrdersByBCMCustomer(Mockito.any()))
		.thenThrow(new BCMModuleException(BCMModuleExceptionEnum.LEASE_NOT_FOUND));
		try {
			jobProcessHelper.processOrderReport(new BCMCustomer());
		} catch(BCMModuleException bcmEx) {
				assertEquals(bcmEx.getErrorCode(), BCMModuleExceptionEnum.LEASE_NOT_FOUND.getCode());
		}
	}
	
	@Test
	public void test_processOrderReportException() {
		List<VendorOrdersResponseModel> orderList = new ArrayList<>();
		VendorOrdersResponseModel orderReponse = TestConstants.getVendorOrdersResponseModelObj();
		orderList.add(orderReponse);
		when(leaseService.getLeaseOrdersByBCMCustomer(Mockito.any()))
		.thenThrow(new NullPointerException());
		try {
		jobProcessHelper.processOrderReport(new BCMCustomer());
		} catch(Exception ex) {
			assertEquals(ex.getMessage(), new NullPointerException().getMessage());
		}
	
	}
	
	
	@Test
	public void test_updateStatus() {
		jobProcessHelper.updateStatus(new CustomerOrderProcess(),OrderProcessStatusEnum.COMPLETED);
		assertNotNull(jobProcessHelper);
	
	}
	
	@Test
	public void test_saveFailedTransactionPullDetails() {
		when(entityBuilder.buildFailedTransactionPull(Mockito.any(),Mockito.any(),Mockito.any())).thenReturn(new FailedTransactionPull());
		jobProcessHelper.saveFailedTransactionPullDetails(new BCMCustomer(),new Job(),"TEST");
		assertNotNull(jobProcessHelper);
	
	}
	
	@Test
	public void test_processBCMCustomer() {
		when(entityBuilder.buildCustomerOrderProcessEntity(Mockito.any(),Mockito.any(),Mockito.any())).thenReturn(new CustomerOrderProcess());
		when(entityBuilder.buildProcessOrderMessageDto(Mockito.any())).thenReturn(new ProcessOrderMessageDTO());
		jobProcessHelper.processBCMCustomer(new BCMCustomer(), new Job());
		assertNotNull(jobProcessHelper);
	
	}
}
