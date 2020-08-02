package com.progrexion.bcm.common.enums;

public enum ResidentStatusEnum {
	
	CUSTOMER_CREATION_INITIATED(0),	
	CUSTOMER_CREATED(1),	
	CUSTOMER_CREATION_FAILED(2),
	;

	private int status;

	ResidentStatusEnum(int status) {
		this.status = status;

	}

	public int getStatus() {
		return status;
	}
	
}
