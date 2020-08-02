package com.progrexion.bcm.models.v1;

import java.io.Serializable;

import javax.validation.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;


@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PaymentAccountRequestDto implements Serializable {

	private static final long serialVersionUID = 1L;
	@NotEmpty
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
