package com.progrexion.bcm.messagingconfig.enums;

public enum MessageStatusEnum {

	PROCESSING("PROCESSING"), SUCCESS("SUCCESS"), FAILURE("FAILURE");

	private String status;

	private MessageStatusEnum(String status) {
		this.status = status;
	}

	public String getStatus() {
		return status;
	}

}
