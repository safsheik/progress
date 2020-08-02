package com.progrexion.bcm.services.builder;


import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.progrexion.bcm.common.exceptions.BCMModuleException;
import com.progrexion.bcm.common.exceptions.BCMModuleExceptionEnum;
import com.progrexion.bcm.common.model.vendor.VendorSubscriptionRequestModel;
import com.progrexion.bcm.common.properties.RentTrackConfigProperties;
import com.progrexion.bcm.common.utils.CommonUtils;


@Component

public class SubscriptionRequestBuilder {

	@Autowired
	private RentTrackConfigProperties property;	
	public VendorSubscriptionRequestModel buildVendorSubscriptionRequestModel(String brand) {

		VendorSubscriptionRequestModel subscriptionRequest = new VendorSubscriptionRequestModel();
		String subscriptionDetails=property.getSubscriptionDetails();	
		String plan = null;
		String promoCode = null;
		Map<String,String> map;
		
		if(subscriptionDetails != null)
		{
			map =  CommonUtils.getHashMapFromString(subscriptionDetails);
			plan = map.get(brand+"_PLAN");
			promoCode = map.get(brand+"_PROMO") ;
		
			if(plan == null || promoCode == null)
			{
				throw new BCMModuleException(BCMModuleExceptionEnum.SUBSCRIPTION_INVALID_PROPERTY);
			}
			subscriptionRequest.setPlan(plan);
			subscriptionRequest.setPromotionCode(promoCode);
		}
		else
		{
			throw new BCMModuleException(BCMModuleExceptionEnum.SUBSCRIPTION_INVALID_PROPERTY);
		}
		return subscriptionRequest;
	}

}
