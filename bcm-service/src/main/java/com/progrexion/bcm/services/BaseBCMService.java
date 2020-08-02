package com.progrexion.bcm.services;

import java.util.Set;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import com.progrexion.bcm.common.AppConstants;
import com.progrexion.bcm.common.enums.SubscriptionStatusEnum;
import com.progrexion.bcm.common.exceptions.BCMModuleException;
import com.progrexion.bcm.common.exceptions.BCMModuleExceptionEnum;
import com.progrexion.bcm.model.entities.BCMCustomer;
import com.progrexion.bcm.model.repositories.CustomerRepository;
import com.progrexion.bcm.model.repositories.CustomerSubscriptionRepository;
import com.progrexion.bcm.model.repositories.FailedDeprovisionRepository;
import com.progrexion.bcm.services.adapter.CustomerMasterAdapter;
import com.progrexion.bcm.services.factory.VendorProcessorFactory;
import com.progrexion.bcm.services.handler.VendorAPILogHandler;
import com.progrexion.bcm.services.persistence.PersistenceService;



public abstract class BaseBCMService {

	@Autowired
	private CustomerMasterAdapter customerMasterAdapter;

	@Autowired
	private PersistenceService persistenceService;
  
  @Autowired
	protected CustomerRepository customerRepository;
	
	@Autowired
	protected VendorProcessorFactory vendorProcessorFactory;
	
	@Autowired
	protected ModelMapper modelMapper;

	@Autowired
	protected VendorAPILogHandler logHandler;
	
	@Autowired
	protected CustomerSubscriptionRepository customerSubscriptionRepository;
	
	@Autowired
	protected FailedDeprovisionRepository failedDeprovisionRepository;
	
	protected Set<Long> fetchCustomerMasterBCMIds(Long ucid) {
		return customerMasterAdapter.getCustomerUCIDs(ucid);	

	}
	
	protected Long fetchCustomerMasterParentUCID(Long ucid) {
		return customerMasterAdapter.getCustomerParentUCID(ucid);	

	}
	
	protected BCMCustomer fetchLatestActiveBCMId(Set<Long> customerMasterUcIds, String brand) {
		BCMCustomer customerObj = null;
		if (!CollectionUtils.isEmpty(customerMasterUcIds)) {
			customerObj = persistenceService.getLatestActiveCustomer(customerMasterUcIds,brand);
		}
		return customerObj;
	}

	protected BCMCustomer fetchLatestActiveCustomer(Long ucid, String brand) {
		BCMCustomer customerObj = null;
		Set<Long> customerMasterIds = fetchCustomerMasterBCMIds(ucid);
		customerObj = fetchLatestActiveBCMId(customerMasterIds, brand);	
		if(customerObj == null)
		{
			throw new BCMModuleException(BCMModuleExceptionEnum.CUSTOMER_INFO_NOT_FOUND);
		}
		return customerObj;

	}

	protected String fetchCustomerStatusInfo(Long ucid, String brand) {
		SubscriptionStatusEnum customerStatusEnum = persistenceService.getCustomerStatustInfo(ucid, brand);
		return null != customerStatusEnum ? customerStatusEnum.name() : AppConstants.NOT_CREATED;
	}
}
