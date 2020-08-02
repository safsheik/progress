package com.progrexion.bcm.renttrack.client.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
@Data
@JsonInclude(Include.NON_NULL)
public class PaymentAccountApiModel implements Serializable {

	private static final long serialVersionUID = 1L;

	@JsonProperty("public_token")
	private String publicToken;
	@JsonProperty("contract_url")
	private String contractUrl;
	@JsonProperty("deposit_account_url")
	private String depositAccountUrl;
	@JsonProperty("pay_type")
	private String payType;
	@JsonProperty("plaid_account_id")
	private String plaidAccountId;
}
