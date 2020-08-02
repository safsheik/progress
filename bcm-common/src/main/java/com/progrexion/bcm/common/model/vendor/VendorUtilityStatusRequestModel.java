package com.progrexion.bcm.common.model.vendor;

import java.io.Serializable;

import lombok.Data;

@Data
public class VendorUtilityStatusRequestModel implements Serializable {

	private static final long serialVersionUID = 1L;

	private String utilityId;
	private String status;

}
