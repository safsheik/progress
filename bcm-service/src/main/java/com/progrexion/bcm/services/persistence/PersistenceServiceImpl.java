package com.progrexion.bcm.services.persistence;

import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import com.progrexion.bcm.common.enums.ResidentStatusEnum;
import com.progrexion.bcm.common.enums.SubscriptionStatusEnum;
import com.progrexion.bcm.common.exceptions.BCMModuleException;
import com.progrexion.bcm.common.exceptions.BCMModuleExceptionEnum;
import com.progrexion.bcm.model.entities.BCMCustomer;
import com.progrexion.bcm.model.entities.ExternalLog;
import com.progrexion.bcm.model.entities.WebServiceLog;
import com.progrexion.bcm.model.repositories.CustomerRepository;
import com.progrexion.bcm.model.repositories.ExternalLogsRepository;
import com.progrexion.bcm.model.repositories.WebServiceLogRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class PersistenceServiceImpl implements PersistenceService {

	@Autowired
	private WebServiceLogRepository webServiceLogRepository;
	@Autowired
	private ExternalLogsRepository externalLogRepository;
	@Autowired
	private CustomerRepository customerRepository;

	@Override
	public WebServiceLog saveWebServiceLogs(WebServiceLog webServiceLogs) {
		WebServiceLog savedWebServiceLog = null;
		try {
			savedWebServiceLog = webServiceLogRepository.save(webServiceLogs);
		} catch (Exception e) {
			log.error("Exception while saving WebServiceLog webServiceLogs ={}", webServiceLogs.toString(), e);
			throwDBAccessException(e);
		}
		return savedWebServiceLog;
	}

	private void throwDBAccessException(Exception e) {
		throw new BCMModuleException(ExceptionUtils.getRootCauseMessage(e),
				BCMModuleExceptionEnum.DB_EXCEPTION);
	}

	@Override
	public void saveExternalLogs(ExternalLog externalLog) {
		try {
			externalLogRepository.save(externalLog);
		} catch (Exception e) {
			log.error("Exception while saving ExternalLog externalLog ={}", externalLog.toString(), e);
			throwDBAccessException(e);
		}
	}
	
	@Override
	public BCMCustomer getLatestActiveCustomer(Collection<Long> bcmIds, String brand) {
		BCMCustomer customer = null;
		Optional<BCMCustomer> opt = customerRepository
				.findTop1ByUcIdInAndBrandOrderByCustomerDataIdDesc
				(bcmIds, brand);
		if(opt.isPresent()) {
		customer = opt.get();
		}
		return customer;
	}

	@Override
	public List<BCMCustomer> getNewAndReportExpiredCustomers(ZonedDateTime transactionPullDate, String brand) {
		return customerRepository
				.findAllByBrandAndTransactionPullDate(transactionPullDate, brand, 
						ResidentStatusEnum.CUSTOMER_CREATED.getStatus(), SubscriptionStatusEnum.ACTIVE);
	}
	
	@Override
	public SubscriptionStatusEnum getCustomerStatustInfo(Long bcmIds, String brand) {
		SubscriptionStatusEnum customerStatus = null;
		Page<SubscriptionStatusEnum> page = customerRepository.findCustomerSubscriptionStatusInfo(bcmIds, brand,
				ResidentStatusEnum.CUSTOMER_CREATED.getStatus(), PageRequest.of(0, 1));
		Optional<SubscriptionStatusEnum> opt = page.stream().findFirst();
		if(opt.isPresent()) {
			customerStatus = opt.get();
		}
		return customerStatus;
	}

}
