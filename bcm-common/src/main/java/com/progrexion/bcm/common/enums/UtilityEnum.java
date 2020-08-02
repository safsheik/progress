package com.progrexion.bcm.common.enums;

public enum UtilityEnum {
	
	GAS("GAS"),
	WATER("WATER"),
	ELECTRIC("ELECTRIC"),
	WIRELESS("WIRELESS"),	
	;

	private String utilityType;

	UtilityEnum(String utilityType) {
		this.utilityType = utilityType;
	}

	public String getUtilityType() {
		return utilityType;
	}

	
}
