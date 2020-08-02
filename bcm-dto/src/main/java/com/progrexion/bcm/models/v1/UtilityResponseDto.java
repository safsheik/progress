package com.progrexion.bcm.models.v1;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY) 
@JsonIgnoreProperties(ignoreUnknown = true)
public class UtilityResponseDto implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@JsonProperty("id")
	private Long id;
	
//	@JsonProperty("url")
//	private String url;
	
	@JsonProperty("type")
	private String type;
	
	@JsonProperty("name")
	private String name;
	
	@JsonProperty("status")
	private String status;
	
	@JsonProperty("reporting_start_at")
	private String reportingStartAt;
	
	@JsonProperty("match_summary")
	private UtilityMatchSummaryDto utilityMatchSummary;

}
