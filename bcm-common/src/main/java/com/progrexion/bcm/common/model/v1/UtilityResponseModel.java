package com.progrexion.bcm.common.model.v1;

import lombok.Data;

@Data

public class UtilityResponseModel {
	
	private Long id;
	private String url;
	private String type;
	private String name;
	private String status;
	private String reportingStartAt;
	private UtilityMatchSummaryModel utilityMatchSummary;
	
	
}
