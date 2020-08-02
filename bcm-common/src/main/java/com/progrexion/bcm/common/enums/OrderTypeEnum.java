package com.progrexion.bcm.common.enums;

public enum OrderTypeEnum {
	
	LEASE("LEASE"),	
	UTILITY("UTILITY"),	
	;

	private String orderType;

	OrderTypeEnum(String orderType) {
		this.orderType = orderType;

	}

	public String getOrderType() {
		return orderType;
	}
	
}
