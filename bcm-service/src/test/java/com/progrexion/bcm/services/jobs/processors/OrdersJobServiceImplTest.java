package com.progrexion.bcm.services.jobs.processors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.progrexion.bcm.common.exceptions.BCMModuleException;
import com.progrexion.bcm.common.exceptions.BCMModuleExceptionEnum;
import com.progrexion.bcm.common.jobs.enums.JobEventTypeEnum;
import com.progrexion.bcm.common.jobs.enums.JobNameEnum;
import com.progrexion.bcm.common.model.v1.ProcessOrderMessageRequestModel;
import com.progrexion.bcm.model.entities.BCMCustomer;
import com.progrexion.bcm.model.entities.CustomerOrderProcess;
import com.progrexion.bcm.model.entities.Job;
import com.progrexion.bcm.model.entities.JobEvent;
import com.progrexion.bcm.model.repositories.CustomerOrderProcessRepository;
import com.progrexion.bcm.model.repositories.CustomerRepository;
import com.progrexion.bcm.model.repositories.JobEventRepository;
import com.progrexion.bcm.model.repositories.JobRepository;
import com.progrexion.bcm.services.constants.TestConstants;
import com.progrexion.bcm.services.v1.CustomerService;

@RunWith(MockitoJUnitRunner.class)
public class OrdersJobServiceImplTest{

	@InjectMocks
	OrdersJobServiceImpl ordersJobServiceImpl;
	
	@Mock
	JobProcessHelper jobProcessHelper;
	
	@Mock
	private JobRepository jobRepository;
	@Mock
	private JobEventRepository jobEventRepository;
	@Mock
	private CustomerService customerService;
	CustomerOrderProcess customerOrderProcess;
	@Mock
	CustomerOrderProcessRepository customerOrderProcessRepo;
	@Mock
	CustomerRepository customerRepo;
	@Test
	public void test_start() {
		ordersJobServiceImpl.start(TestConstants.getNewJobObj());
		assertNotNull(ordersJobServiceImpl);
	}
	
	@Test
	public void test_start1() {
		List<BCMCustomer> bcmCustomers = new ArrayList<>();
		bcmCustomers.add(TestConstants.getCustomerObject());
		when(jobProcessHelper.getAllCustomersToGetOrders(Mockito.any(), Mockito.any())).thenReturn(bcmCustomers);
		ordersJobServiceImpl.start(TestConstants.getNewJobObj());
		assertNotNull(ordersJobServiceImpl);
	}
	
	@Test
	public void test_start4() {
		when(jobProcessHelper.getAllCustomersToGetOrders(Mockito.any(), Mockito.any()))
			.thenThrow(new BCMModuleException(BCMModuleExceptionEnum.BCM_MODULE_EXCEPTION));
		try {
			ordersJobServiceImpl.start(TestConstants.getNewJobObj());
		} catch(BCMModuleException ex) {
			assertEquals(ex.getErrorCode(),BCMModuleExceptionEnum.BCM_MODULE_EXCEPTION.getCode());
		}
	}
	
	@Test
	public void test_start5() {

		when(jobProcessHelper.getAllCustomersToGetOrders(Mockito.any(), Mockito.any()))
			.thenThrow(new NullPointerException());
		ordersJobServiceImpl.start(TestConstants.getNewJobObj());
		assertNotNull(ordersJobServiceImpl);
	}
	@Test
	public void test_start6() {
		Job job = null;
		try {
			ordersJobServiceImpl.start(job);
		} catch(BCMModuleException ex) {
			assertEquals(ex.getErrorCode(),BCMModuleExceptionEnum.JOB_EXCEPTION.getCode());
		}
	}
	@Test
	public void test_getJobName() {
		
		assertEquals(JobNameEnum.RENTTRACK_REPORTED_ORDERS, ordersJobServiceImpl.getJobName()) ;
	}
   
	@Test
	public void test_createJob(){
		when(jobRepository.save(Mockito.any())).thenReturn(TestConstants.getNewJobObj());
		assertNotNull(ordersJobServiceImpl.createJob("CCOM"));
	}

	@Test
	public void test_status(){
		List<Job> jobs = new ArrayList<>();
		jobs.add(TestConstants.getNewJobObj());
		when(jobRepository.findByJobNameAndBrand(Mockito.any(),Mockito.any())).thenReturn(jobs);
		assertNotNull(ordersJobServiceImpl.status("CCOM",null));
	}
	@Test
	public void test_status1(){
		List<Job> jobs = new ArrayList<>();
		jobs.add(TestConstants.getNewJobObj());
		when(jobRepository.findByJobName(Mockito.any())).thenReturn(jobs);
		assertNotNull(ordersJobServiceImpl.status(null,null));
	}
	
	@Test
	public void test_status2(){
		Job job = TestConstants.getNewJobObj();
		job.setJobEvents(new ArrayList<>());
		job.getJobEvents().add(new JobEvent(JobEventTypeEnum.PROCESS_SUBMITTED, job));
		Optional<Job> jobOpt = Optional.of(job);
		when(jobRepository.findById(Mockito.any())).thenReturn(jobOpt);
		when(customerOrderProcessRepo.findAllByJobAndStatusIn(Mockito.any(), Mockito.any())).thenReturn(new ArrayList<>());
		assertNotNull(ordersJobServiceImpl.status("CCOM",1l));
	}
	
	@Test
	public void test_statusWithJobName(){
		List<Job> jobs = new ArrayList<>();
		jobs.add(TestConstants.getNewJobObj());
		when(jobRepository.findByJobNameAndBrand(Mockito.any(),Mockito.any())).thenReturn(jobs);
		assertNotNull(ordersJobServiceImpl.status(JobNameEnum.RENTTRACK_REPORTED_ORDERS, "CCOM",null));
	}
	@Test
	public void test_statusWithJobName1(){
		List<Job> jobs = new ArrayList<>();
		jobs.add(TestConstants.getNewJobObj());
		when(jobRepository.findByJobName(Mockito.any())).thenReturn(jobs);
		assertNotNull(ordersJobServiceImpl.status(JobNameEnum.RENTTRACK_REPORTED_ORDERS, null,null));
	}
	
	@Test
	public void test_statusWithJobName2(){
		Optional<Job> jobOpt = Optional.of(TestConstants.getNewJobObj());
		when(jobRepository.findById(Mockito.any())).thenReturn(jobOpt);
		assertNotNull(ordersJobServiceImpl.status(JobNameEnum.RENTTRACK_REPORTED_ORDERS,"CCOM",1l));
	}
	@Test
	public void test_statusWithJobName3(){
		Optional<Job> jobOpt = Optional.of(TestConstants.getNewJobObj());
		when(jobRepository.findById(Mockito.any())).thenReturn(jobOpt);
		try {
		ordersJobServiceImpl.status(JobNameEnum.RENTTRACK_REPORTED_ORDERS,"DUMMY",1l);
		} catch (BCMModuleException e) {
			
		assertEquals(BCMModuleExceptionEnum.JOB_NOT_FOUND.getCode(), e.getErrorCode());
		}
	}
	@Test
	public void test_statusWithJob(){
		assertNotNull(ordersJobServiceImpl.status(TestConstants.getNewJobObj()));
	}
	@Test
	public void test_statusWithJob1(){
		assertNull(ordersJobServiceImpl.status(null));
	}
	@Test
	public void test_statusOrderCheck(){
		assertNotNull(ordersJobServiceImpl.status(TestConstants.getNewJobObj()));
	}
	@Test
	public void test_performStop(){
		Job job = TestConstants.getNewJobObj();
		when(jobEventRepository.findByJobAndJobEventCode(Mockito.any(),Mockito.any())).thenReturn(TestConstants.getJobEventObj());
		when(jobRepository.save(Mockito.any())).thenReturn(job);
		try {
			ordersJobServiceImpl.performStop(job);
			} catch (BCMModuleException e) {
				
			assertEquals(BCMModuleExceptionEnum.JOB_NOT_FOUND.getCode(), e.getErrorCode());
			}
	}
	
	@Test
	public void test_requestStop(){
		try {
			ordersJobServiceImpl.requestStop(null,null);
			} catch (BCMModuleException e) {
				
			assertEquals(BCMModuleExceptionEnum.JOB_NOT_FOUND.getCode(), e.getErrorCode());
			}
	}
	
	@Test
	public void test_requestStop1(){
		try {
			ordersJobServiceImpl.requestStop(1l,null);
			} catch (BCMModuleException e) {
				
			assertEquals(BCMModuleExceptionEnum.JOB_NOT_FOUND.getCode(), e.getErrorCode());
			}
	}
	
	@Test
	public void test_processOrderReport(){
			ProcessOrderMessageRequestModel model = new ProcessOrderMessageRequestModel();
			model.setCustomerDataId(101l);
			model.setCustomerOrderProcessId(102l);
			model.setJobId(102l);
			customerOrderProcess =new CustomerOrderProcess();
			customerOrderProcess.setJob(TestConstants.getNewJobObj());
			customerOrderProcess.setCustomer(TestConstants.getCustomerObject());
			Optional<CustomerOrderProcess> customerOrderProcessOpt = Optional.of(customerOrderProcess);
			when(customerOrderProcessRepo.findById(Mockito.any())).thenReturn(customerOrderProcessOpt);
			doNothing().when(jobProcessHelper).updateStatus(Mockito.any(),Mockito.any());
			
			ordersJobServiceImpl.processOrderReport(model);
			assertNotNull(ordersJobServiceImpl);
	}
	
	@Test
	public void test_processOrderReport1(){
		try {
			ProcessOrderMessageRequestModel model = new ProcessOrderMessageRequestModel();
			model.setCustomerDataId(100l);
			model.setCustomerOrderProcessId(102l);
			model.setJobId(103l);
			customerOrderProcess =new CustomerOrderProcess();
			customerOrderProcess.setJob(TestConstants.getNewJobObj());
			customerOrderProcess.setCustomer(TestConstants.getCustomerObject());
			Optional<CustomerOrderProcess> customerOrderProcessOpt = Optional.of(customerOrderProcess);
			when(customerOrderProcessRepo.findById(Mockito.any())).thenReturn(customerOrderProcessOpt);
			doNothing().when(jobProcessHelper).updateStatus(Mockito.any(),Mockito.any());
			
			ordersJobServiceImpl.processOrderReport(model);

			} catch (BCMModuleException e) {
				
			assertEquals(BCMModuleExceptionEnum.JOB_NOT_FOUND.getCode(), e.getErrorCode());
			}
	}
	
	@Test
	public void test_processOrderReport2(){
		try {
			ProcessOrderMessageRequestModel model = new ProcessOrderMessageRequestModel();
			model.setCustomerDataId(101l);
			model.setCustomerOrderProcessId(102l);
			model.setJobId(103l);
			customerOrderProcess =new CustomerOrderProcess();
			customerOrderProcess.setJob(TestConstants.getNewJobObj());
			customerOrderProcess.setCustomer(TestConstants.getCustomerObject());
			Optional<CustomerOrderProcess> customerOrderProcessOpt = Optional.of(customerOrderProcess);
			when(customerOrderProcessRepo.findById(Mockito.any())).thenReturn(customerOrderProcessOpt);
			doNothing().when(jobProcessHelper).updateStatus(Mockito.any(),Mockito.any());
			
			ordersJobServiceImpl.processOrderReport(model);

			} catch (BCMModuleException e) {
				
			assertEquals(BCMModuleExceptionEnum.CUSTOMER_INFO_NOT_FOUND.getCode(), e.getErrorCode());
			}
	}
	
	@Test
	public void test_processOrderReport3(){
		try {
			ProcessOrderMessageRequestModel model = new ProcessOrderMessageRequestModel();
			model.setCustomerDataId(101l);
			model.setCustomerOrderProcessId(102l);
			model.setJobId(102l);
			customerOrderProcess =new CustomerOrderProcess();
			customerOrderProcess.setJob(TestConstants.getNewJobObj());
			customerOrderProcess.setCustomer(TestConstants.getCustomerObject());
			Optional<CustomerOrderProcess> customerOrderProcessOpt = Optional.of(customerOrderProcess);
			when(customerOrderProcessRepo.findById(Mockito.any())).thenReturn(customerOrderProcessOpt);
			when(jobProcessHelper.processOrderReport(Mockito.any())).thenThrow(new BCMModuleException(BCMModuleExceptionEnum.JOB_NOT_FOUND));
			doNothing().when(jobProcessHelper).updateStatus(Mockito.any(),Mockito.any());
			
			ordersJobServiceImpl.processOrderReport(model);

			} catch (BCMModuleException e) {
				
			assertEquals(BCMModuleExceptionEnum.JOB_NOT_FOUND.getCode(), e.getErrorCode());
			}
	}
	
	@Test
	public void test_processOrderReport4(){
		try {
			ProcessOrderMessageRequestModel model = new ProcessOrderMessageRequestModel();
			model.setCustomerDataId(101l);
			model.setCustomerOrderProcessId(102l);
			model.setJobId(102l);
			customerOrderProcess =new CustomerOrderProcess();
			customerOrderProcess.setJob(TestConstants.getNewJobObj());
			customerOrderProcess.setCustomer(TestConstants.getCustomerObject());
			Optional<CustomerOrderProcess> customerOrderProcessOpt = Optional.of(customerOrderProcess);
			when(customerOrderProcessRepo.findById(Mockito.any())).thenReturn(customerOrderProcessOpt);
			when(jobProcessHelper.processOrderReport(Mockito.any())).thenThrow(new NullPointerException());
			doNothing().when(jobProcessHelper).updateStatus(Mockito.any(),Mockito.any());
			
			ordersJobServiceImpl.processOrderReport(model);

			} catch (BCMModuleException e) {
				
			assertEquals(BCMModuleExceptionEnum.JOB_NOT_FOUND.getCode(), e.getErrorCode());
			}
	}
}
