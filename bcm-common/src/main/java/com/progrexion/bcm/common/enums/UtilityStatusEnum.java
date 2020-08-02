package com.progrexion.bcm.common.enums;

public enum UtilityStatusEnum {
	
	UTILITY_STATUS_VALUE("current"),	
	
	;

	private String status;

	UtilityStatusEnum(String status) {
		this.status = status;

	}

	public String getStatus() {
		return status;
	}
	

}
