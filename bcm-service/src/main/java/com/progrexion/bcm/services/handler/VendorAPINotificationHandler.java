package com.progrexion.bcm.services.handler;

import com.progrexion.bcm.common.processor.VendorAPINotification;
import com.progrexion.bcm.model.entities.BCMCustomer;

public class VendorAPINotificationHandler implements VendorAPINotification {

	private BCMCustomer customer;
	private Long vendorId;
	private VendorAPILogHandler logHandler;

	public VendorAPINotificationHandler(BCMCustomer customer, Long vendorId, VendorAPILogHandler logHandler) {
		this.customer = customer;
		this.vendorId = vendorId;
		this.logHandler = logHandler;
	}
	@Override
	public void notify(String url, String request, String response, String httpMethod) {
		logHandler.handle(customer, vendorId, url, request, response, httpMethod);
	}
	
	@Override
	public void addActivity(String requestType) {
		logHandler.addActivity(customer, requestType);
	}

}
