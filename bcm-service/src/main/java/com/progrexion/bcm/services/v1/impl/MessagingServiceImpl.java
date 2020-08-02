package com.progrexion.bcm.services.v1.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.progrexion.bcm.common.enums.SubscriptionStatusEnum;
import com.progrexion.bcm.common.exceptions.BCMModuleException;
import com.progrexion.bcm.common.exceptions.BCMModuleExceptionEnum;
import com.progrexion.bcm.common.model.v1.CancelSubscriptionMessageRequestModel;
import com.progrexion.bcm.common.model.vendor.VendorSubscriptionResponseModelV2;
import com.progrexion.bcm.common.model.vendor.enums.RentTrackClientRequestTypesEnum;
import com.progrexion.bcm.common.processor.VendorAPINotification;
import com.progrexion.bcm.messagingconfig.enums.BCMVendorEnum;
import com.progrexion.bcm.model.entities.BCMCustomer;
import com.progrexion.bcm.model.entities.CustomerSubscription;
import com.progrexion.bcm.model.entities.FailedDeprovision;
import com.progrexion.bcm.services.BaseBCMService;
import com.progrexion.bcm.services.builder.EntityDataBuilder;
import com.progrexion.bcm.services.handler.VendorAPINotificationHandler;
import com.progrexion.bcm.services.jobs.processors.JobProcessHelper;
import com.progrexion.bcm.services.v1.MessagingService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class MessagingServiceImpl extends BaseBCMService implements MessagingService {
	
	@Autowired
	private EntityDataBuilder entityBuilder;
	
	@Autowired
	JobProcessHelper jobProcessHelper;

	@Override
	public Boolean processCancelSubscriptionMessage(CancelSubscriptionMessageRequestModel requestModel,	String pbsMessage) {
		log.info("MessagingServiceImpl::processCancelSubscriptionMessage::Input:[{}]", requestModel);

		String brand = null;
		Long activeSubscriptionId = null;
		Boolean isSubscriptionCancelled = false;
		BCMCustomer bcmCustomer = null;
		String errorMessage = null;
		List<Long> ucids = new ArrayList<>();
		VendorAPINotification apiNotificationHandler = null;
		try {
			brand = requestModel.getBrand();
			ucids.add(requestModel.getUcid());
			if (null != requestModel.getMergedUcids() && !requestModel.getMergedUcids().isEmpty())
				ucids.addAll(requestModel.getMergedUcids());

			log.info("Check if this Customer record exists in BCM with UCIDs: [{}], and BRAND: [{}]", ucids, brand);

			bcmCustomer = customerRepository.findFirstByUcIdInAndBrandOrderByCreatedDateDesc(ucids, brand);

			if (null == bcmCustomer) {
				log.error("Cannot find Customer record in BCM database with UCIDs:[{}], and BRAND: [{}]", ucids, brand);
				throw new BCMModuleException(BCMModuleExceptionEnum.CANNOT_FIND_CUSTOMER_BCM_MESSAGING_EXCEPTION);
			}

			log.info("Customer info: customer_data_id: [{}], ucid: [{}], brand: [{}]", bcmCustomer.getCustomerDataId(),	bcmCustomer.getUcId(), bcmCustomer.getBrand());

			apiNotificationHandler = new VendorAPINotificationHandler(bcmCustomer, BCMVendorEnum.RENTTRACK.getId(), logHandler);

			List<VendorSubscriptionResponseModelV2> subscriptions = vendorProcessorFactory
					.getVendorProcessor(BCMVendorEnum.RENTTRACK)
					.getActiveSubscriptions(bcmCustomer.getCustomerDataId(), apiNotificationHandler);

			if (null == subscriptions || subscriptions.isEmpty() || null == subscriptions.get(0).getId()) {
				log.error("No active subscriptions to cancel. [{}]", subscriptions);
				throw new BCMModuleException(BCMModuleExceptionEnum.NO_ACTIVE_SUBSCRIPTIONS_BCM_MESSAGING_EXCEPTION);
			}

			log.info("Cancelling RentTrack Subscription started: [{}]", subscriptions.get(0));

			activeSubscriptionId = subscriptions.get(0).getId();
			boolean isOrderSaved = jobProcessHelper.processOrderReport(bcmCustomer);
			if(isOrderSaved) {
				log.info("MessagingServiceImpl: saveOrdersReport to Save Customer orders infomation in BCM Database - completed");
				isSubscriptionCancelled = deleteSubscription(bcmCustomer, activeSubscriptionId, apiNotificationHandler);
				log.info("Cancelling RentTrack Subscription done: [{}]", subscriptions.get(0));
			} else {
				log.info("MessagingServiceImpl: saveOrdersReport to Save Customer orders infomation in BCM Database - not completed, so deprovisioning not started!");
			}
			
			if (isSubscriptionCancelled) {
				cancelBCMSubscription(bcmCustomer, activeSubscriptionId);
				apiNotificationHandler.addActivity(RentTrackClientRequestTypesEnum.SUBSCRIPTIONS_DELETE.getName());
				log.info("MessagingServiceImpl::processCancelSubscriptionMessage::Create activity log entry for Cancel Subcsription.");
			} else {
				log.info("MessagingServiceImpl::processCancelSubscriptionMessage::Create activity log entry not added for Cancel Subcsription.");			
			}
			
		} catch (BCMModuleException bcmEx) {
			errorMessage =bcmEx.getErrorMessage();
			log.error("BCMModuleException occurred processCancelSubscriptionMessage: Error Message={}", bcmEx.getErrorMessage());
		} catch (Exception e) {
			errorMessage = e.getMessage();
			log.info("Exception Occurred while cancelling subscription:  Error Message= {}", e.getMessage());
		} finally {
			if (null != errorMessage) {
				saveFailedDeprovisionSubscription(bcmCustomer, pbsMessage, errorMessage);
			}
		}		
		return isSubscriptionCancelled;
	}
	
	private boolean deleteSubscription(BCMCustomer bcmCustomer, Long activeSubscriptionId,
			VendorAPINotification apiNotificationHandler) {
		Boolean isSubscriptionCancelled = false;
		isSubscriptionCancelled = vendorProcessorFactory.getVendorProcessor(BCMVendorEnum.RENTTRACK).deleteSubscription(bcmCustomer.getCustomerDataId(), activeSubscriptionId, apiNotificationHandler);
		log.info("MessagingServiceImpl::processCancelSubscriptionMessage::STATUS: [{}]", (isSubscriptionCancelled ? "SUCCESS" : "FAILURE"));
		return isSubscriptionCancelled;
	}
		
	public boolean cancelBCMSubscription(BCMCustomer bcmCustomer, Long activeSubscriptionId) {
		CustomerSubscription bcmSubscription = new CustomerSubscription();
		boolean isBCMSubscriptionCancelled = false;
		try {
			log.info("MessagingServiceImpl: updateBCMSubscription BCMCustomerSubscription: BCMSubscriptionCancelled changed status INACTIVE.");
			bcmSubscription = customerSubscriptionRepository.findFirstByCustomerOrderByCreatedDateDesc(bcmCustomer);
			if (null != bcmSubscription) {
				bcmSubscription.setStatus(SubscriptionStatusEnum.INACTIVE);

			} else {
				bcmSubscription = entityBuilder.buildCustomerSubscription(bcmCustomer, activeSubscriptionId, SubscriptionStatusEnum.INACTIVE);
			}

			customerSubscriptionRepository.save(bcmSubscription);
			isBCMSubscriptionCancelled = true;

		} catch (Exception e) {
			return isBCMSubscriptionCancelled;
		}
		return isBCMSubscriptionCancelled;
	}
	
	public void saveFailedDeprovisionSubscription(BCMCustomer bcmCustomer, String pbsMessage, String errorMessage) {
		FailedDeprovision failedDeprovision = new FailedDeprovision();
		try {
			log.info("MessagingServiceImpl: saveFailedDeprovisionSubscription to Save failure infomation BCM Database");
			failedDeprovision = entityBuilder.buildCustomerSubscriptionFailedDeprovision(bcmCustomer, pbsMessage, errorMessage);
			failedDeprovisionRepository.save(failedDeprovision);

		} catch (Exception e) {
			throw new BCMModuleException(BCMModuleExceptionEnum.FAILED_DEPROVISION_NOT_SAVE);
		}

	}
}
