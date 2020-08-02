package com.progrexion.bcm.common.enums;

public enum CustomerActivityEnum {
	
	CREATE_TENANT_CUSTACTIVITY(0,true),
	PATCH_DOB_CUSTACTIVITY(1,true),
	SUBSCRIPTION_CUSTACTIVITY(2,true),
	SUBSCRIPTIONS_DELETE_CUSTACTIVITY(3,true),
	CREATE_LEASE_CUSTACTIVITY(4,true),
	DELETE_LEASE_CUSTACTIVITY(5,true),
	CREATE_PAYMENT_ACCOUNT_CUSTACTIVITY(6,true),
	PLAID_RECONNECTION_CUSTACTIVITY(7,true),
	CREATE_TRX_FINDER_CUSTACTIVITY(8,true),
	MATCH_TRX_FINDER_CUSTACTIVITY(9,true),
	SEARCH_TRX_FINDER_CUSTACTIVITY(10,true),
	UTILITY_OPT_IN_WATER_CUSTACTIVITY(11,true),
	UTILITY_OPT_IN_ELECTRIC_CUSTACTIVITY(12,true),
	UTILITY_OPT_IN_GAS_CUSTACTIVITY(13,true),
	UTILITY_OPT_IN_WIRELESS_CUSTACTIVITY(14,true),
	PLAID_RECONNECTION_PATCH_CUSTACTIVITY(15,true),
	;

	private int activityType;
	private boolean isRequired;

	CustomerActivityEnum(int activityType, boolean isRequired) {
		this.activityType = activityType;
		this.isRequired = isRequired;
	}

	public int getActivityType() {
		return activityType;
	}

	public boolean isRequired() {
		return isRequired;
	}
	
}
