package com.progrexion.bcm.models.v1;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TransactionRequestDto {

	@JsonProperty("rent_amount")
	private float rentAmount;

	@JsonProperty("due_day")
	private int dueDay;

	@JsonProperty("leaseUrl")
	private String leaseUrl;

	@JsonProperty("paymentAccountUrl")
	private String paymentAccountUrl;
	
	@JsonProperty("transactionFinderId")
	private Long transactionFinderId;

}
