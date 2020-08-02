package com.progrexion.bcm.common.model.vendor;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY) 
@JsonIgnoreProperties(ignoreUnknown = true)
public class VendorUtilityMatchSummaryModel implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@JsonProperty("count")
	private String count;
	
	@JsonProperty("newest")
	private VendorUtilityTransactionMatchModel newest;
	
	@JsonProperty("oldest")
	private VendorUtilityTransactionMatchModel oldest;
	
	

}
