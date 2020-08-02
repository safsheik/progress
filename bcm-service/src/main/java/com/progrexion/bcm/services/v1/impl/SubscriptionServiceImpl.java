package com.progrexion.bcm.services.v1.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.progrexion.bcm.common.model.v1.SubscriptionRequestModel;
import com.progrexion.bcm.common.model.v1.SubscriptionResponseModel;
import com.progrexion.bcm.common.model.vendor.VendorSubscriptionRequestModel;
import com.progrexion.bcm.common.model.vendor.VendorSubscriptionResponseModel;
import com.progrexion.bcm.common.processor.VendorAPINotification;
import com.progrexion.bcm.common.processor.VendorProcessor;
import com.progrexion.bcm.messagingconfig.enums.BCMVendorEnum;
import com.progrexion.bcm.model.entities.BCMCustomer;
import com.progrexion.bcm.services.BaseBCMService;
import com.progrexion.bcm.services.builder.SubscriptionRequestBuilder;
import com.progrexion.bcm.services.handler.VendorAPINotificationHandler;
import com.progrexion.bcm.services.v1.SubscriptionService;
import com.progrexion.bcm.services.validator.SubscriptionValidator;

@Service
public class SubscriptionServiceImpl extends BaseBCMService  implements SubscriptionService {

	@Autowired
	private SubscriptionValidator validator;
	@Autowired
	private SubscriptionRequestBuilder subscriptionRequestBuilder;
	@Override
	public SubscriptionResponseModel createSubscription(long ucid, String brand,
			SubscriptionRequestModel subscriptionRequest) {
		BCMCustomer customer = fetchLatestActiveCustomer(ucid,brand);
		validator.validateCustomer(customer);
		VendorSubscriptionResponseModel vendorResponse = processSubscriptionRequest(customer);
		return constructSubscriptionResponseModel(vendorResponse);
	}
	
	
	private SubscriptionResponseModel constructSubscriptionResponseModel(VendorSubscriptionResponseModel vendorResponse) {
		SubscriptionResponseModel response=new SubscriptionResponseModel();
		response.setPaymentAccountUrl(vendorResponse.getPaymentAccountUrl());
		response.setPeriod(vendorResponse.getPeriod());
		response.setStatus(vendorResponse.getStatus());
		response.setSubscriptionId(vendorResponse.getSubscriptionId());
		response.setPlanName(vendorResponse.getPlanName());
		return response;
	}
	
	
	private VendorSubscriptionResponseModel processSubscriptionRequest(BCMCustomer customer) {
		VendorSubscriptionRequestModel request = subscriptionRequestBuilder.buildVendorSubscriptionRequestModel(customer.getBrand());
		VendorAPINotification apiNotificationHandler = new VendorAPINotificationHandler(customer,
				BCMVendorEnum.RENTTRACK.getId(), logHandler);
		return vendorProcessorFactory.getVendorProcessor(BCMVendorEnum.RENTTRACK).createSubscription(request,customer.getCustomerDataId(),apiNotificationHandler);

	}

	@Override
	public VendorSubscriptionResponseModel processSubscriptionRequest(BCMCustomer customer, VendorProcessor vendorProcessor) {
		VendorSubscriptionRequestModel request = subscriptionRequestBuilder.buildVendorSubscriptionRequestModel(customer.getBrand());
		VendorAPINotification apiNotificationHandler = new VendorAPINotificationHandler(customer,
				BCMVendorEnum.RENTTRACK.getId(),logHandler);
		return vendorProcessor.createSubscription(request,customer.getCustomerDataId(),apiNotificationHandler);
	}
	
	@Override
	public SubscriptionResponseModel[] getSubscriptionsInfo(Long ucid, String brand) {

		BCMCustomer customer = fetchLatestActiveCustomer(ucid,brand);
		VendorSubscriptionResponseModel[] vendorResponse = getAllSubscriptionRequest(customer);
		return new ModelMapper().map(vendorResponse, SubscriptionResponseModel[].class);
	}
	
	public SubscriptionResponseModel[] getSubscriptionsInfo(BCMCustomer customer) {
		VendorSubscriptionResponseModel[] vendorResponse = getAllSubscriptionRequest(customer);
		return new ModelMapper().map(vendorResponse, SubscriptionResponseModel[].class);
	}

	private VendorSubscriptionResponseModel[] getAllSubscriptionRequest(BCMCustomer customer) {

		VendorAPINotification apiNotificationHandler = new VendorAPINotificationHandler(customer,
				BCMVendorEnum.RENTTRACK.getId(), logHandler);

		return vendorProcessorFactory.getVendorProcessor(BCMVendorEnum.RENTTRACK)
				.getSubscriptionsInfo(customer.getCustomerDataId(), apiNotificationHandler);
	}
	
}
