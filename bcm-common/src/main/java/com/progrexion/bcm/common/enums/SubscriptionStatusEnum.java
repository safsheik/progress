package com.progrexion.bcm.common.enums;

public enum SubscriptionStatusEnum {

	ACTIVE(1), 
	INACTIVE(0),;

	private int status;

	SubscriptionStatusEnum(int status) {
		this.status = status;

	}

	public int getStatus() {
		return status;
	}

}
