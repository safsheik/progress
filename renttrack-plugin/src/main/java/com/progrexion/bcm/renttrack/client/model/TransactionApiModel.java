package com.progrexion.bcm.renttrack.client.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

@Data
@JsonInclude(Include.NON_NULL)
public class TransactionApiModel implements Serializable {

	private static final long serialVersionUID = 1L;

	@JsonProperty("amount_min")
	private float amountMin;
	
	@JsonProperty("amount_max")
	private float amountMax;

	@JsonProperty("window_open")
	private int windowOpen;
	
	@JsonProperty("window_close")
	private int windowClose;

	@JsonProperty("payment_account_url")
	private String paymentAccountUrl;

	@JsonProperty("lease_url")
	private String leaseUrl;

	@JsonProperty("leaseId")
	private Long leaseId;
	
	@JsonProperty("transaction_finder_id")
	private Long transactionFinderId;

}
