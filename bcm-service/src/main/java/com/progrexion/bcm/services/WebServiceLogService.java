package com.progrexion.bcm.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.progrexion.bcm.common.model.WebServiceLogsModel;
import com.progrexion.bcm.services.builder.EntityDataBuilder;
import com.progrexion.bcm.services.persistence.PersistenceService;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@Service
public class WebServiceLogService {

	@Autowired
	private PersistenceService persistenceService;

	@Autowired
	private EntityDataBuilder entityDataBuilder;

	@Async
	public void saveWebServiceDetails(WebServiceLogsModel webServiceLogsModel) {

		try {
			persistenceService.saveWebServiceLogs(entityDataBuilder.getWebServiceLogsEntity(webServiceLogsModel));

		} catch (Exception ex) {
			log.error("Exception while saving the Web Service Incoming requet and Respone details", ex);
		}

	}

}
