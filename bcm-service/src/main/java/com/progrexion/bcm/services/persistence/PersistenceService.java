package com.progrexion.bcm.services.persistence;

import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.List;

import com.progrexion.bcm.common.enums.SubscriptionStatusEnum;
import com.progrexion.bcm.model.entities.BCMCustomer;
import com.progrexion.bcm.model.entities.ExternalLog;
import com.progrexion.bcm.model.entities.WebServiceLog;

public interface PersistenceService {

	WebServiceLog saveWebServiceLogs(WebServiceLog webServiceLogs);

	void saveExternalLogs(ExternalLog externalLog);

	BCMCustomer getLatestActiveCustomer(Collection<Long> bcmIds, String brand);

	List<BCMCustomer> getNewAndReportExpiredCustomers(ZonedDateTime reportDate, String brand);
	
	SubscriptionStatusEnum getCustomerStatustInfo(Long bcmIds, String brand);

}
