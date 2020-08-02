package com.progrexion.bcm.common.processor;


public interface VendorAPINotification {

	public void notify(String url, String request, String response, String httpMethod);

	public void addActivity(String requestType);

}
