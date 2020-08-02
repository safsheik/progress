package com.progrexion.bcm.services.validator;

import org.springframework.beans.factory.annotation.Autowired;

import com.progrexion.bcm.common.exceptions.BCMModuleException;
import com.progrexion.bcm.common.exceptions.BCMModuleExceptionEnum;
import com.progrexion.bcm.common.model.v1.SubscriptionResponseModel;
import com.progrexion.bcm.model.entities.BCMCustomer;
import com.progrexion.bcm.services.v1.SubscriptionService;

import lombok.extern.slf4j.Slf4j;
@Slf4j
public class BCMValidator {

	@Autowired
	private SubscriptionService subscriptionService;

		
	public void validateCustomer(BCMCustomer customer)
	{	
		SubscriptionResponseModel[] subscriptionResponse = subscriptionService
				.getSubscriptionsInfo(customer);
		if (subscriptionResponse.length == 0) {
			log.info("Subscription not availble");
			throw new BCMModuleException(BCMModuleExceptionEnum.CUSTOMER_SUBSCRIPTION_FAILED);
		}
	}
	public boolean verifyCustomerSubscription(BCMCustomer customer)
	{	
		boolean isSubscribed = false;
		SubscriptionResponseModel[] subscriptionResponse = subscriptionService
				.getSubscriptionsInfo(customer);
		if (subscriptionResponse.length != 0) {
			isSubscribed = true;
			log.info("Subscription is availble");
		}
		
		return isSubscribed;
	}
	
	public SubscriptionResponseModel[] getCustomerRTSubscription(BCMCustomer customer) {
		return subscriptionService.getSubscriptionsInfo(customer);
	}

}
