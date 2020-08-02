package com.progrexion.bcm.services.v1;

import com.progrexion.bcm.common.model.v1.SubscriptionRequestModel;
import com.progrexion.bcm.common.model.v1.SubscriptionResponseModel;
import com.progrexion.bcm.common.model.vendor.VendorSubscriptionResponseModel;
import com.progrexion.bcm.common.processor.VendorProcessor;
import com.progrexion.bcm.model.entities.BCMCustomer;

public interface SubscriptionService {

	public SubscriptionResponseModel createSubscription(long ucid, String brand,
			SubscriptionRequestModel subscriptionRequest);

	public VendorSubscriptionResponseModel processSubscriptionRequest(BCMCustomer customer, VendorProcessor vendorProccessor);

	public SubscriptionResponseModel[] getSubscriptionsInfo(Long ucid, String brand);
	
	public SubscriptionResponseModel[] getSubscriptionsInfo(BCMCustomer customer);
	
}