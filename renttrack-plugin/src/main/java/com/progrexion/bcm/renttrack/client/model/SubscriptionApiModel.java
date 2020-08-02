package com.progrexion.bcm.renttrack.client.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
@Data
@JsonInclude(Include.NON_NULL)
public class SubscriptionApiModel implements Serializable {

	private static final long serialVersionUID = 1L;

	@JsonProperty("payment_account_url")
	private String paymentAccountUrl;

	@JsonProperty("plan_name")
	private String planName;

	@JsonProperty("uuid")
	private String uuid;

	@JsonProperty("period")
	private String period;

	@JsonProperty("promotion_code")
	private String promotionCode;

	@JsonProperty("uuid_required")
	private boolean uuidRequired;
}
