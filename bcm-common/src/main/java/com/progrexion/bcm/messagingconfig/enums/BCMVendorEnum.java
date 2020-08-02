package com.progrexion.bcm.messagingconfig.enums;

import org.apache.commons.lang.StringUtils;

public enum BCMVendorEnum {

	RENTTRACK(1, "RentTrack"), 

	;

	private long id;
	private String name;

	private BCMVendorEnum(long id, String name) {
		this.id = id;
		this.name = name;
	}

	public long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public static BCMVendorEnum fromValue(String value) {
		if (!StringUtils.isBlank(value)) {
			BCMVendorEnum[] bcmVendorEnums = BCMVendorEnum.values();
			for (BCMVendorEnum bcmVendorEnum : bcmVendorEnums) {
				if (bcmVendorEnum.getName().equals(value)) {
					return bcmVendorEnum;
				}
			}
		}
		return RENTTRACK;
	}

}
