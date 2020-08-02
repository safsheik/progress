package com.progrexion.bcm.common.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@Data
@ConfigurationProperties(prefix = "renttrack.uri")
public class RentTrackAPIConfigProperties {

	private String baseUrl;
	private String createSubscription;
	private String createUser;
	private String updateUser;
	private String createLease;
	private String createPlaidPaymentAccount;
	private String paymentAccount;
	private String createTrxFinderByLeaseId;
	private String searchTrxFinderByLeaseId;
	private String matchTrxFinderByLeaseId;
	private String searchTrxById;
	private String leaseOrders;
	private String subscriptions;
	private String getActiveSubscriptions;
	private String deleteSubscription;
	private String getAllUtilities;
	private String getUtilityByTradeLineId;
	private String patchUtility;
	private String allSubscriptions;
	private String updateTrxById;
	private String plaidReconnectByPayAccId;
	private String utilityOrders;
	private String transactionFinder;
	
	
	
}
