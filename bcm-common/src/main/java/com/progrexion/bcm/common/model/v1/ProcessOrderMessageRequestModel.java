package com.progrexion.bcm.common.model.v1;

import lombok.Data;

@Data
public class ProcessOrderMessageRequestModel {
	private Long customerOrderProcessId;
	private Long customerDataId;
	private Long jobId;
}
