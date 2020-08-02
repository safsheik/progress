package com.progrexion.bcm.services.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.progrexion.bcm.model.entities.BCMCustomer;
import com.progrexion.bcm.model.entities.CustomerActivity;
import com.progrexion.bcm.model.entities.ExternalLog;
import com.progrexion.bcm.services.builder.EntityDataBuilder;
import com.progrexion.bcm.services.persistence.PersistenceService;
import com.progrexion.bcm.services.v1.CustomerActivityService;

@Component
public class VendorAPILogHandler {

	@Autowired
	private EntityDataBuilder entityDataBuilder;

	@Autowired
	private PersistenceService persistenceService;
	
	@Autowired
	private CustomerActivityService customerActivityService;

	public void handle(BCMCustomer customer, Long vendorId, String url, String request, String response, String httpMethod) {
		ExternalLog externalLog = entityDataBuilder.buildExternalLogs(customer, vendorId, url, request, response, httpMethod);
		persistenceService.saveExternalLogs(externalLog);
	}
	
	public void addActivity(BCMCustomer customer, String requestType) {
		if(requestType != null)
		{
			CustomerActivity customerActivity = entityDataBuilder.buildCustomerActivity(customer, requestType);
			if(customerActivity!=null)
			{
				customerActivityService.saveCustomerActivity(customerActivity);
			}			
		}
	}
}
