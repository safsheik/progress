package com.progrexion.bcm.models.v1;

import java.io.Serializable;

import javax.validation.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UtilityStatusRequestDto implements Serializable {

	private static final long serialVersionUID = 1L;

	@NotEmpty
	@JsonProperty("utility_trade_id")
	private String utilityId;
	
	@JsonProperty("status")
	private String status;
	
	@NotEmpty
	@JsonProperty("opt_for")
	private String optFor;

}
