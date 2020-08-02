package com.progrexion.bcm.models.v1;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY) 
@JsonIgnoreProperties(ignoreUnknown = true)
public class MatchTransactionResponseDto implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Long id;
	private String status;
	
	@JsonProperty("reference_id")
	private String referenceId;
	
	@JsonProperty("payment_source")
	private String paymentSource;
	
	private String type;
	private String message;
	
	@JsonProperty("paid_for")
	private String paidFor;
	
	@JsonProperty("deposited_at")
	private String depositedAt;
	
	@JsonProperty("reported_at")
	private String reportedAt;
	
	@JsonProperty("delivery_method")
	private String deliveryMethod;

}
