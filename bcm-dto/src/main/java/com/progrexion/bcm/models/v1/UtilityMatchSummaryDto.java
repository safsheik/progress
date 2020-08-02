package com.progrexion.bcm.models.v1;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY) 
@JsonIgnoreProperties(ignoreUnknown = true)
public class UtilityMatchSummaryDto implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@JsonProperty("count")
	private String count;
	
	@JsonProperty("newest")
	private UtilityTransactionMatchDto newest;
	
	@JsonProperty("oldest")
	private UtilityTransactionMatchDto oldest;
	
	

}
