package com.progrexion.bcm.services.jobs.processors;

import java.time.ZonedDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.progrexion.bcm.common.exceptions.BCMModuleException;
import com.progrexion.bcm.common.exceptions.BCMModuleExceptionEnum;
import com.progrexion.bcm.common.jobs.enums.JobEventTypeEnum;
import com.progrexion.bcm.common.jobs.enums.JobNameEnum;
import com.progrexion.bcm.common.jobs.enums.OrderProcessStatusEnum;
import com.progrexion.bcm.common.model.v1.ProcessOrderMessageRequestModel;
import com.progrexion.bcm.common.utils.DateUtils;
import com.progrexion.bcm.model.entities.BCMCustomer;
import com.progrexion.bcm.model.entities.CustomerOrderProcess;
import com.progrexion.bcm.model.entities.Job;
import com.progrexion.bcm.model.entities.JobEvent;
import com.progrexion.bcm.model.repositories.CustomerOrderProcessRepository;
import com.progrexion.bcm.model.repositories.CustomerRepository;
import com.progrexion.bcm.model.repositories.JobRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service("ordersJobServiceImpl")
public class OrdersJobServiceImpl extends JobServiceImpl implements JobService {

	@Autowired
	JobProcessHelper jobProcessHelper;
	
	@Autowired
	private JobRepository jobRepository;
	
	@Autowired
	private CustomerRepository custRepo;
	
	@Autowired
	private CustomerOrderProcessRepository customerOrderProcessRepo;

	@Override
	public void start(Job job) {
		log.info("In: start()");
		try {
			processRTReportedOrdersJob(job);
			job.getJobEvents().add(new JobEvent(JobEventTypeEnum.PROCESS_SUBMITTED, job));
			job.setModifiedDate();
			job.setPeriodEndDate(job.getModifiedDate());
			jobRepository.save(job);
		} catch (BCMModuleException e) {
			log.error("Unable to process job: {}",e);
			throw e;
		} catch (Exception e) {
			log.error("Unable to process job: {}",e);
			if (null != job) {
				job.getJobEvents().add(new JobEvent(JobEventTypeEnum.ERRORED, job));
				job.setModifiedDate();
				job.setPeriodEndDate(job.getModifiedDate());
				jobRepository.save(job);
			} else {
				throw new BCMModuleException(BCMModuleExceptionEnum.JOB_EXCEPTION);
			}
		}
		log.info("Out: start()");
	}

	private void processRTReportedOrdersJob(Job job) {

		String brand = job.getBrand();
		ZonedDateTime lastPullDate = DateUtils.addDaysToTheYear(jobProcessHelper.getNumberOfDaystoCheck(job.getJobName()));
		log.info("Report to be Generated from : " + lastPullDate);	
		try {
			List<BCMCustomer> customerList = jobProcessHelper
					.getAllCustomersToGetOrders(lastPullDate, brand);					
			for (BCMCustomer bcmCustomer : customerList) {	
				super.performStop(job);
				jobProcessHelper.processBCMCustomer(bcmCustomer, job);
			}
		} catch (BCMModuleException bcmEx) {
			log.error("BCMModuleException Occurred during Expired Job. Exception Message={}", bcmEx.getErrorMessage());
			throw bcmEx;
		} catch (Exception ex) {
			log.error("Exception Occurred during Expired Job. Exception Message={}", ex.getMessage());
			throw ex;
		}

	}
		
	@Override
	public JobNameEnum getJobName() {
		return JobNameEnum.RENTTRACK_REPORTED_ORDERS;
	}

	@Override
	public void processOrderReport(ProcessOrderMessageRequestModel processOrderMessageRequestModel) {
		OrderProcessStatusEnum processStatus =  OrderProcessStatusEnum.FAILED;
		boolean isJobSuccess = false;
		BCMCustomer bcmCustomer = null;	
		Job job = null ;
		CustomerOrderProcess customerOrderProcess = null;
		String errorMessage = null;
		try {
			log.info("processOrderReport() : Started");
			customerOrderProcess = customerOrderProcessRepo
						.findById(processOrderMessageRequestModel.getCustomerOrderProcessId()).orElseThrow();
			
			jobProcessHelper.updateStatus(customerOrderProcess, OrderProcessStatusEnum.PROCESSING);	
			bcmCustomer = customerOrderProcess.getCustomer();
			if( !bcmCustomer.getCustomerDataId()
					.equals(processOrderMessageRequestModel.getCustomerDataId()) ) {
				throw new BCMModuleException(BCMModuleExceptionEnum.CUSTOMER_INFO_NOT_FOUND);
			}	
			job = customerOrderProcess.getJob();
			if( !job.getJobId()
					.equals(processOrderMessageRequestModel.getJobId()) ) {
				throw new BCMModuleException(BCMModuleExceptionEnum.JOB_NOT_FOUND);
			}
			isJobSuccess = jobProcessHelper.processOrderReport(bcmCustomer);
			if(isJobSuccess) {
				processStatus = OrderProcessStatusEnum.COMPLETED;
			} 
			bcmCustomer.setTransactionPullDate();
			custRepo.save(bcmCustomer);
			log.info("Transaction Pull Date Updated for the customer Id {}", 
					bcmCustomer.getCustomerDataId());	
		} catch (BCMModuleException bcmEx) {
			log.error("BCMModuleException occurred when processing customer order Id ={}. Error Message={}", 
			processOrderMessageRequestModel.getCustomerOrderProcessId(), bcmEx.getErrorMessage(), bcmEx);
			errorMessage = bcmEx.getErrorMessage();	
			processStatus = OrderProcessStatusEnum.ERROR;
		} catch (Exception ex) {
			log.error("UnKnown Exception occurred when processing customer order Id ={}. Error Message={}",
			processOrderMessageRequestModel.getCustomerOrderProcessId(), ex.getMessage(), ex);
			errorMessage = ex.getLocalizedMessage();
			processStatus = OrderProcessStatusEnum.ERROR;
		}
		finally {
			jobProcessHelper.updateStatus(customerOrderProcess, processStatus);
			if(null != errorMessage && null != job) {
				jobProcessHelper.saveFailedTransactionPullDetails(bcmCustomer, job, errorMessage);
			}
		}
	}






}
