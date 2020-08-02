package com.progrexion.bcm.common.model.vendor;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;
@Data
@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class VendorSubscriptionResponseModel implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@JsonProperty("id")
	private Long subscriptionId;
	@JsonProperty("url")
	private String url;
	@JsonProperty("payment_account_url")
	private String paymentAccountUrl;
	@JsonProperty("plan_name")
	private String planName;
	@JsonProperty("plan_features")
	private String[] planFeatures;	
	@JsonProperty("period")
	private String period;
	@JsonProperty("status")
	private String status;
	@JsonProperty("last_payment_date")
	private Date lastPaymentDate;
	@JsonProperty("next_payment_date")
	private Date nextPaymentDate;
	@JsonProperty("next_payment_amount")
	private Double nextPaymentAmount;
	@JsonProperty("discounted_until")
	private Date discountedUntil;
	@JsonProperty("free_until")
	private Date freeUntil;
	@JsonProperty("enabled_at")
	private Date enabledAt;
	@JsonProperty("cancelled_at")
	private Date cancelledAt;
	@JsonProperty("external_property_id")
	private String externalPropertyId;

}
