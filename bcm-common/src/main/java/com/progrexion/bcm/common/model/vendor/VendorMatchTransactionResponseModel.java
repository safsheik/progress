package com.progrexion.bcm.common.model.vendor;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class VendorMatchTransactionResponseModel implements Serializable {

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
	
	@JsonProperty("delivery_method")
	private String deliveryMethod;

}
