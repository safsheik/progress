package com.progrexion.bcm.messagingconfig.enums;

public enum MessageDestinationTypeEnum {

	CUSTOMER_ALERTS("CUSTOMER_ALERTS");

	private String destinationType;

	private MessageDestinationTypeEnum(String destinationType) {
		this.destinationType = destinationType;
	}

	public String getDestinationType() {
		return destinationType;
	}

}
