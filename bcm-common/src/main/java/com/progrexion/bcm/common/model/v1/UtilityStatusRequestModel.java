package com.progrexion.bcm.common.model.v1;

import lombok.Data;

@Data
public class UtilityStatusRequestModel{
	private String utilityId;
	private String status;
	private String optFor;
}
