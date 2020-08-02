package com.progrexion.bcm.services.jobs.processors;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.progrexion.bcm.common.enums.OrderTypeEnum;
import com.progrexion.bcm.common.exceptions.BCMModuleException;
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
import com.progrexion.bcm.services.jobs.publisher.CustomerProcessOrderMessagePublisher;
import com.progrexion.bcm.services.persistence.PersistenceService;
import com.progrexion.bcm.services.v1.LeaseService;
import com.progrexion.bcm.services.v1.UtilityService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class JobProcessHelper {

	@Autowired
	private PersistenceService persistenceService;

	@Autowired
	private LeaseService leaseService;
	
	@Autowired
	private UtilityService utilityService;
	
	@Autowired
	private EntityDataBuilder entityBuilder;
	
	@Autowired
	private OrderRepository orderRepo;
	
	@Autowired
	private FailedTransactionPullRepository failedTransactionPullRepo;
	
	@Autowired
	private BCMConfigProperties bcmProperty;

	@Autowired
	private CustomerOrderProcessRepository customerOrderProcessRepo;
	
	@Autowired
	private CustomerProcessOrderMessagePublisher messagePublisher;
	/**
	 * This method gives all the customer details to which are going to get the orders from RentTrack
	 * 
	 * @param startDate
	 * @param endDate
	 * @returnO
	 */
	public List<BCMCustomer> getAllCustomersToGetOrders(ZonedDateTime reportDate, String brand) {
		return persistenceService.getNewAndReportExpiredCustomers(reportDate, brand);
	}

/**
	 * THis method gives the number of days to check before or after current date based on the Job Names
	 * @param jobName
	 * @return
	 */
	public int getNumberOfDaystoCheck(JobNameEnum jobName) {
		int days = 0;
		if(jobName.equals(JobNameEnum.RENTTRACK_REPORTED_ORDERS)){
			days = bcmProperty.getTransactionPullCycleInDays()+1;  
		}
		return days;
	}

	public boolean processOrderReport(BCMCustomer bcmCustomer){
		boolean isSuccess = false;
		try
		{
			List<VendorOrdersResponseModel> leaseOrders = leaseService
				.getLeaseOrdersByBCMCustomer(bcmCustomer);	
			log.info("Lease orders for the customer data id ={} and the No. of Lease orders from RentTrack [{}]",
					bcmCustomer.getUcId(), leaseOrders.size());
			
			if( !leaseOrders.isEmpty() ) {
				buildAndSaveOrderEntity(OrderTypeEnum.LEASE, leaseOrders, 
							bcmCustomer);										
			}
			List<VendorOrdersResponseModel> utilityOrders = utilityService
					.getUtilityOrdersByBCMCustomer(bcmCustomer);	
			log.info("Utility orders for the customer data id ={} and the No. of Utility orders from RentTrack [{}]",
						bcmCustomer.getUcId(), utilityOrders.size());
			
			if( !utilityOrders.isEmpty() ) {
				buildAndSaveOrderEntity(OrderTypeEnum.UTILITY, utilityOrders, 
						bcmCustomer);										
			}
			isSuccess = true;
			log.info("Pull Orders Request processed for the customer Id [{}]",
					bcmCustomer.getCustomerDataId());
		} catch(BCMModuleException bcmEx) {
			log.info("BCM Exception occurred while processing : "+bcmEx.getErrorMessage());	
			throw bcmEx;
		} catch(Exception ex) {			
			log.info("Exception occurred while processing : "+ex.getMessage());
			throw ex;
		} 
		return isSuccess;
	}
	
	private void buildAndSaveOrderEntity(OrderTypeEnum orderTypeEnum, List<VendorOrdersResponseModel> rtOrderList, 
			BCMCustomer bcmCustomer) {
		List<Order> ordersList = new ArrayList<>();
		Order orderObj = null;
		for(VendorOrdersResponseModel order : rtOrderList ) {
			orderObj = entityBuilder.buildOrderEntity(order, bcmCustomer, orderTypeEnum);
			ordersList.add(orderObj);
		}	
		if(!ordersList.isEmpty()) {
			log.info("Number of {} Orders to be stored [{}]:",orderTypeEnum.getOrderType(), ordersList.size());		
			orderRepo.saveAll(ordersList);
		}
	}
	
	
	public void processBCMCustomer(BCMCustomer bcmCustomer, Job job) {
		Long ucid = bcmCustomer.getUcId();
		log.info("Going to process ucid={}",ucid);
		String errorMessage = null;
		ProcessOrderMessageDTO processOrderMessageDTO = null;
		OrderProcessStatusEnum processStatus = null;
		CustomerOrderProcess customerOrderProcess = null;
		try { 
			customerOrderProcess = entityBuilder.buildCustomerOrderProcessEntity(bcmCustomer,
					 job, OrderProcessStatusEnum.PENDING);
			customerOrderProcess = customerOrderProcessRepo.save(customerOrderProcess);
			log.info("Customer Order Process Saved succesfully");
			processOrderMessageDTO = entityBuilder.buildProcessOrderMessageDto(customerOrderProcess);
			messagePublisher.publish(processOrderMessageDTO);	
			log.info("Customer Order Process Message Published succesfully for the Id {}",customerOrderProcess.getId());
		} catch(BCMModuleException bcmEx) {
			errorMessage = bcmEx.getErrorMessage();	
			log.info("BCM Exception occurred while processing : "+errorMessage);
			processStatus = OrderProcessStatusEnum.ERROR;
		} catch(Exception ex) {			
			errorMessage = ex.getLocalizedMessage();
			log.info("Exception occurred while processing : "+errorMessage);
			processStatus = OrderProcessStatusEnum.ERROR;
		} finally {
			if(null != errorMessage) {
				saveFailedTransactionPullDetails(bcmCustomer, job, errorMessage);
			}
			updateStatus(customerOrderProcess,processStatus);
		}

	}
	
	public  void saveFailedTransactionPullDetails(BCMCustomer bcmCustomer, Job job, String errorMessage)
	{
		FailedTransactionPull failedTrxPull = entityBuilder.buildFailedTransactionPull(bcmCustomer,job,errorMessage);
		failedTransactionPullRepo.save(failedTrxPull);
	}
	
	public void updateStatus(CustomerOrderProcess customerOrderProcess, OrderProcessStatusEnum processStatus) {
		if(null != customerOrderProcess && null != processStatus ) {
			customerOrderProcess.setStatus(processStatus);
			customerOrderProcess.setModifiedDate();
			customerOrderProcessRepo.save(customerOrderProcess);			
		}

	}
}
