package com.progrexion.bcm.renttrack.serviceimpl;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.progrexion.bcm.common.AppConstants;
import com.progrexion.bcm.common.model.vendor.VendorCreateUserRequestModel;
import com.progrexion.bcm.common.model.vendor.VendorCreateUserResponseModel;
import com.progrexion.bcm.common.model.vendor.VendorLeaseRequestModel;
import com.progrexion.bcm.common.model.vendor.VendorLeaseResponseModel;
import com.progrexion.bcm.common.model.vendor.VendorMatchTransactionRequestModel;
import com.progrexion.bcm.common.model.vendor.VendorMatchTransactionResponseModel;
import com.progrexion.bcm.common.model.vendor.VendorOrdersResponseModel;
import com.progrexion.bcm.common.model.vendor.VendorPaymentAccountRequestModel;
import com.progrexion.bcm.common.model.vendor.VendorPaymentAccountResponseModel;
import com.progrexion.bcm.common.model.vendor.VendorPlaidReconnectResponseModel;
import com.progrexion.bcm.common.model.vendor.VendorSearchTransactionResponseModel;
import com.progrexion.bcm.common.model.vendor.VendorSubscriptionRequestModel;
import com.progrexion.bcm.common.model.vendor.VendorSubscriptionResponseModel;
import com.progrexion.bcm.common.model.vendor.VendorSubscriptionResponseModelV2;
import com.progrexion.bcm.common.model.vendor.VendorTransactionRequestModel;
import com.progrexion.bcm.common.model.vendor.VendorTransactionResponseModel;
import com.progrexion.bcm.common.model.vendor.VendorUpdateUserRequestModel;
import com.progrexion.bcm.common.model.vendor.VendorUtilityDetailsResponseModel;
import com.progrexion.bcm.common.model.vendor.VendorUtilityResponseModel;
import com.progrexion.bcm.common.model.vendor.VendorUtilityStatusRequestModel;
import com.progrexion.bcm.common.model.vendor.enums.RentTrackClientRequestTypesEnum;
import com.progrexion.bcm.common.processor.VendorAPINotification;
import com.progrexion.bcm.common.processor.VendorProcessor;
import com.progrexion.bcm.renttrack.client.RentTrackClient;
import com.progrexion.bcm.renttrack.client.model.LeaseApiModel;
import com.progrexion.bcm.renttrack.client.model.MatchTransactionArrayApiModel;
import com.progrexion.bcm.renttrack.client.model.PaymentAccountApiModel;
import com.progrexion.bcm.renttrack.client.model.RTCreateUserResponseModel;
import com.progrexion.bcm.renttrack.client.model.SubscriptionApiModel;
import com.progrexion.bcm.renttrack.client.model.TransactionApiModel;
import com.progrexion.bcm.renttrack.client.model.UtilityStatusApiModel;
import com.progrexion.bcm.renttrack.client.model.builder.RentTrackModelBuilder;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@Qualifier("renttrackprocessor")
public class RentTrackProcessor implements VendorProcessor {

	@Autowired
	private RentTrackModelBuilder rentTrackModelBuilder;

	@Autowired
	private RentTrackClient rentTrackServiceClient;

	@Autowired
	private ModelMapper modelMapper;

	/**
	 * Subscription Creation
	 * @throws IOException
	 * @throws JsonMappingException
	 * @throws JsonParseException
	 */
	@Override
	public VendorSubscriptionResponseModel createSubscription(VendorSubscriptionRequestModel request, Long customerId, VendorAPINotification apiNotification) {
		SubscriptionApiModel subscriptionApiModel = rentTrackModelBuilder.buildCreateSubscriptionApiModel(request);
		VendorSubscriptionResponseModel response = rentTrackServiceClient.createSubscription(subscriptionApiModel,
				customerId, apiNotification);
		if (null != response && null != response.getSubscriptionId()) {
		addActivity(apiNotification,RentTrackClientRequestTypesEnum.SUBSCRIPTION.getName());
		}
		return response;
	}

	/**
	 * Construct RT request and invoke createSubscription api
	 * 
	 * @param request
	 * @throws IOException
	 * @throws JsonMappingException
	 * @throws JsonParseException
	 * 
	 */
	@Override
	public VendorCreateUserResponseModel createUserAccount(VendorCreateUserRequestModel request, VendorAPINotification apiNotification) {
		RTCreateUserResponseModel rtResponse = rentTrackServiceClient
				.createUserAccount(rentTrackModelBuilder.createUserAccountApiRequestModel(request), apiNotification);
		return modelMapper.map(rtResponse, VendorCreateUserResponseModel.class);
	}

	@Override
	public Boolean updateTenantUserDetails(Long customerId, VendorUpdateUserRequestModel request, VendorAPINotification apiNotification) {
		Boolean isUpdated = rentTrackServiceClient.updateTenantUserDetails(customerId, request, apiNotification);
		if (isUpdated) {
				addActivity(apiNotification,RentTrackClientRequestTypesEnum.PATCH_DOB.getName());
		}
		return isUpdated;
	}

	/**
	 * Lease Creation
	 */
	@Override
	public VendorLeaseResponseModel createLease(VendorLeaseRequestModel request, Long customerId, VendorAPINotification apiNotification) {
		LeaseApiModel leaseApiModel = rentTrackModelBuilder.buildLeaseApiModel(request);
		VendorLeaseResponseModel response = rentTrackServiceClient.createLease(leaseApiModel, customerId,
				apiNotification);
		if (null != response && null != response.getLeaseId()) {
						addActivity(apiNotification,RentTrackClientRequestTypesEnum.CREATE_LEASE.getName());
		}
		return response;
	}

	/**
	 * Get Lease info List
	 */

	@Override
	public VendorLeaseResponseModel getLeaseInfo(Long customerId, VendorAPINotification apiNotification) {
		return rentTrackServiceClient.getLeaseList(customerId, apiNotification);
	}

	/**
	 * Payment Account Creation
	 * 
	 * @throws IOException
	 * @throws JsonMappingException
	 * @throws JsonParseException
	 */
	@Override
	public VendorPaymentAccountResponseModel[] createPlaidPaymentAccount(VendorPaymentAccountRequestModel request, Long customerId, VendorAPINotification apiNotification) {
		PaymentAccountApiModel paymentAccountApiModel = rentTrackModelBuilder
				.buildCreatePaymentAccountApiModel(request);
		VendorPaymentAccountResponseModel[] response = rentTrackServiceClient
				.createPlaidPaymentAccount(paymentAccountApiModel, customerId, apiNotification);
		if (null != response && response.length > 0) {
			addActivity(apiNotification,RentTrackClientRequestTypesEnum.CREATE_PAYMENT_ACCOUNT.getName());
		}
		return response;
	}

	/**
	 * Get Plaid PaymentAccount Information
	 */
	@Override
	public VendorPaymentAccountResponseModel getPlaidPaymentAccountDetails(Long customerId, VendorAPINotification apiNotification) {
		return rentTrackServiceClient.getPlaidPaymentAccountDetails(customerId, apiNotification);
	}

	/**
	 * Create TransactionFinder
	 */
	@Override
	public VendorTransactionResponseModel createTransactionFinder(VendorTransactionRequestModel request, Long customerId, Long leaseId, VendorAPINotification apiNotification) {
		TransactionApiModel transactionApiModel = rentTrackModelBuilder.buildTransactionApiModel(request);
		VendorTransactionResponseModel response = rentTrackServiceClient.createTransactionFinder(transactionApiModel,
				customerId, apiNotification);
		if (null != response && null != response.getId()) {
			addActivity(apiNotification,RentTrackClientRequestTypesEnum.CREATE_TRX_FINDER.getName());
		}
		return response;
	}

	/**
	 * Search By TransactionFinder
	 */
	@Override
	public VendorSearchTransactionResponseModel[] searchByTransactionFinder(VendorTransactionRequestModel request, Long customerId, VendorAPINotification apiNotification) {
		TransactionApiModel transactionApiModel = rentTrackModelBuilder.buildTransactionApiModel(request);
		VendorSearchTransactionResponseModel[] responseArray = rentTrackServiceClient
				.searchTransactionInfoByFinder(transactionApiModel, customerId, apiNotification);
		if (responseArray.length != 0) {
		addActivity(apiNotification,RentTrackClientRequestTypesEnum.SEARCH_TRX_FINDER.getName());
		}
		return responseArray;
	}

	@Override
	public VendorMatchTransactionResponseModel[] matchTransaction(VendorMatchTransactionRequestModel request, Long customerId, Long transactionId, VendorAPINotification apiNotification) {
		MatchTransactionArrayApiModel matchTransactionApiModel = rentTrackModelBuilder.buildMatchTrxApiModel(request);
		VendorMatchTransactionResponseModel[] response = rentTrackServiceClient
				.matchTransaction(matchTransactionApiModel, customerId, transactionId, apiNotification);
		if (null != response && response.length > 0) {
				addActivity(apiNotification,RentTrackClientRequestTypesEnum.MATCH_TRX_FINDER.getName());
		}
		return response;
	}

	/**
	 * Get Transaction By Lease
	 */
	@Override
	public VendorSearchTransactionResponseModel[] searchTransactionByFinder(Long customerId, Long transactionId, VendorAPINotification apiNotification) {
		TransactionApiModel transactionApiModel = rentTrackModelBuilder.buildSearchTransactionApiModel(transactionId);
		VendorSearchTransactionResponseModel[] response = rentTrackServiceClient
				.searchTransactionByFinder(transactionApiModel, customerId, apiNotification);
		if (response.length != 0) {
			addActivity(apiNotification,RentTrackClientRequestTypesEnum.SEARCH_TRX_FINDER.getName());
		}
		return response;
	}

	/**
	 * Get Lease Orders List
	 */

	@Override
	public VendorOrdersResponseModel[] getLeaseOrders(Long leaseId, Long customerId, VendorAPINotification apiNotification) {
		return rentTrackServiceClient.getLeaseOrders(leaseId, customerId, apiNotification);
	}

	@Override
	public VendorUtilityResponseModel[] getAllUtilities(Long customerId, VendorAPINotification apiNotificationHandler) {
		return rentTrackServiceClient.getAllUtilities(customerId, apiNotificationHandler);
	}

	@Override
	public VendorUtilityDetailsResponseModel[] getUtilityDetails(Long customerId, Long utilityTradeLineId, VendorAPINotification apiNotificationHandler) {
		return rentTrackServiceClient.getUtilityDetails(customerId, utilityTradeLineId, apiNotificationHandler);
	}

	@Override
	public boolean updateUtiltyStatusRequest(Long customerId, String utilityName, VendorUtilityStatusRequestModel vendorRequest, VendorAPINotification apiNotificationHandler) {
		Boolean isUpdated = false;
		UtilityStatusApiModel utiltyStatusApiModel = rentTrackModelBuilder.buildUtiltyStatusApiModel(vendorRequest);
		isUpdated = rentTrackServiceClient.updateUtiltyStatus(customerId, vendorRequest.getUtilityId(), utiltyStatusApiModel,
				apiNotificationHandler);
		if (isUpdated) {
			addActivity(apiNotificationHandler,RentTrackClientRequestTypesEnum.UTILITY_OPT_IN.getName()+AppConstants.UNDERSCORE+utilityName);
		}
		return isUpdated;
	}

	private void addActivity(VendorAPINotification apiNotification, String requestType) {
		try {
			apiNotification.addActivity(requestType);
		} catch (Exception e) {
			log.error("Unable to add Activity. Error in while inserting request", e.getMessage(), e);
		}
	}

	/**
	 * Get UserSubscriptions info
	 */
	@Override
	public VendorSubscriptionResponseModel[] getSubscriptionsInfo(Long customer, VendorAPINotification apiNotification) {
		return rentTrackServiceClient.getSubscriptionsInfo(customer, apiNotification);
	}

	@Override
	public List<VendorSubscriptionResponseModelV2> getActiveSubscriptions(Long customerId, VendorAPINotification apiNotificationHandler) {
		try {
			return rentTrackServiceClient.getActiveSubscriptions(customerId, apiNotificationHandler);
		} catch (Exception e) {
			log.info("Exception Occurred while getting active subscriptions: {}", e.getMessage());
		}
		return null;
	}

	@Override
	public Boolean deleteSubscription(Long customerId, Long subscriptionId, VendorAPINotification apiNotificationHandler) {
			return rentTrackServiceClient.deleteSubscription(customerId, subscriptionId, apiNotificationHandler);
	}

	/**
	 * Update TransactionFinder
	 */
	@Override
	public boolean updateTransactionFinder(VendorTransactionRequestModel request, Long customerId, Long transactionFinderId, VendorAPINotification apiNotification) {
		Boolean isUpdated = false;
		TransactionApiModel transactionApiModel = rentTrackModelBuilder.buildTransactionUpdateApiModel(request);
		isUpdated = rentTrackServiceClient.updateTransactionFinder(transactionApiModel, customerId, transactionFinderId,
				apiNotification);

		if (isUpdated) {
			addActivity(apiNotification, RentTrackClientRequestTypesEnum.UPDATE_TRX_FINDER.getName());
		}
		return isUpdated;

	}

	@Override
	public VendorPlaidReconnectResponseModel reconnectPlaid(Long customerId, Long paymentAccId, VendorAPINotification apiNotification, String reqType) {
		VendorPlaidReconnectResponseModel response = rentTrackServiceClient.reconnectPlaid(customerId, paymentAccId,
				apiNotification, reqType);
		addActivity(apiNotification, reqType);
		return response;
	}

	@Override
	public List<VendorOrdersResponseModel> getUtilityOrders(Long customerId, Long utilityId,
			VendorAPINotification apiNotification) {
		VendorOrdersResponseModel[] response = rentTrackServiceClient.getUtilityOrders(customerId, utilityId,
				apiNotification);
		return Arrays.asList(response);
	}

	@Override
	public VendorTransactionResponseModel getTransactionFinderById(Long customerId, Long transactionFinderId,
			VendorAPINotification apiNotification) {
		return rentTrackServiceClient.getTransactionFinder(customerId, transactionFinderId,
				apiNotification);
	}

}
