package com.progrexion.bcm.common.processor;

import java.util.List;

import com.progrexion.bcm.common.model.vendor.VendorCreateUserRequestModel;
import com.progrexion.bcm.common.model.vendor.VendorCreateUserResponseModel;
import com.progrexion.bcm.common.model.vendor.VendorOrdersResponseModel;
import com.progrexion.bcm.common.model.vendor.VendorLeaseRequestModel;
import com.progrexion.bcm.common.model.vendor.VendorLeaseResponseModel;
import com.progrexion.bcm.common.model.vendor.VendorMatchTransactionRequestModel;
import com.progrexion.bcm.common.model.vendor.VendorMatchTransactionResponseModel;
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

public interface VendorProcessor {

	VendorSubscriptionResponseModel createSubscription(VendorSubscriptionRequestModel request,Long customerId, VendorAPINotification apiNotification);

	VendorCreateUserResponseModel createUserAccount(VendorCreateUserRequestModel request,VendorAPINotification apiNotification);

	Boolean updateTenantUserDetails(Long customerId,  VendorUpdateUserRequestModel request,VendorAPINotification apiNotification);

	VendorLeaseResponseModel createLease(VendorLeaseRequestModel request, Long customerId, VendorAPINotification apiNotification) ;
	VendorLeaseResponseModel getLeaseInfo(Long customerId, VendorAPINotification apiNotification);

	VendorPaymentAccountResponseModel[] createPlaidPaymentAccount(VendorPaymentAccountRequestModel request, Long customerId, VendorAPINotification apiNotification);

	VendorPaymentAccountResponseModel getPlaidPaymentAccountDetails(Long customerId,
			VendorAPINotification apiNotification);

	VendorTransactionResponseModel createTransactionFinder(VendorTransactionRequestModel request, Long customerId,
			Long leaseId, VendorAPINotification apiNotification) ;
	
	VendorSearchTransactionResponseModel[] searchByTransactionFinder(VendorTransactionRequestModel request,Long customerId,
			VendorAPINotification apiNotification) ;

	VendorMatchTransactionResponseModel[] matchTransaction(VendorMatchTransactionRequestModel request, Long customerId,
			Long transactionId, VendorAPINotification apiNotification);
	
	VendorSearchTransactionResponseModel[] searchTransactionByFinder(Long customerId,
			Long transactionId, VendorAPINotification apiNotification) ;

	VendorOrdersResponseModel[] getLeaseOrders(Long leaseId,
			Long customerId, VendorAPINotification apiNotification);
	
	VendorUtilityResponseModel[] getAllUtilities(Long customerId, VendorAPINotification apiNotificationHandler);

	VendorUtilityDetailsResponseModel[] getUtilityDetails(Long customerId, Long utilityTradeLineId,
			VendorAPINotification apiNotificationHandler);

	boolean updateUtiltyStatusRequest(Long customerId, String utilityName,
			VendorUtilityStatusRequestModel vendorRequest, VendorAPINotification apiNotificationHandler);

	VendorSubscriptionResponseModel[] getSubscriptionsInfo(Long customerId,
			VendorAPINotification apiNotification);

	List<VendorSubscriptionResponseModelV2> getActiveSubscriptions(Long customerId, VendorAPINotification apiNotificationHandler);

	Boolean deleteSubscription(Long customerId, Long subscriptionId, VendorAPINotification apiNotificationHandler);
			
	boolean updateTransactionFinder(VendorTransactionRequestModel request, Long customerId, Long transactionFinderId,
			VendorAPINotification apiNotification);
	
	VendorPlaidReconnectResponseModel reconnectPlaid(Long customerDataId, Long paymentAccountId, VendorAPINotification apiNotificationHandler, String reqType);
	
	List<VendorOrdersResponseModel> getUtilityOrders(Long customerId, Long utilityId,
			VendorAPINotification apiNotificationHandler);

	VendorTransactionResponseModel getTransactionFinderById(Long customerDataId, Long transactionFinderId,
			VendorAPINotification apiNotificationHandler);
}
