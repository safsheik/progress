package com.progrexion.bcm.common.model.vendor.enums;

import org.apache.commons.lang.StringUtils;

public enum VendorProviderEnum {
	RENTTRACK("RentTrack"),;

	private String name;

	private VendorProviderEnum(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public static VendorProviderEnum fromValue(String value) {
		if (!StringUtils.isBlank(value)) {
			VendorProviderEnum[] vendorProviderEnums = VendorProviderEnum.values();
			for (VendorProviderEnum vendorProviderEnum : vendorProviderEnums) {
				if (vendorProviderEnum.getName().equals(value)) {
					return vendorProviderEnum;
				}
			}
		}
		return RENTTRACK;
	}

}
