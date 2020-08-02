package com.progrexion.bcm.messagingconfig.enums;

public enum BCMStatusEnum {

	PENDING(0), PROCESSING(1), SUCCESS(2), FAILURE(3),

	;

	private int id;

	private BCMStatusEnum(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

}
