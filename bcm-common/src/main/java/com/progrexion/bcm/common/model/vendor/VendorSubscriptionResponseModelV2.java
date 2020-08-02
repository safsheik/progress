package com.progrexion.bcm.common.model.vendor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
@Data
@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class VendorSubscriptionResponseModelV2 implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private Long id;
	
	private String url;
	
	@JsonProperty("payment_account_url")
	private String paymentAccountUrl;
	
	@JsonProperty("plan_name")
	private String planName;
	
	@JsonProperty("plan_features")
	private List<String> planFeatures;
	
	private String period;
	
	private String status;
	
	@JsonProperty("last_payment_date")
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date lastPaymentDate;
	
	@JsonProperty("next_payment_date")
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date nextPaymentDate;
	
	@JsonProperty("next_payment_amount")
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Double nextPaymentAmount;
	
	@JsonProperty("discounted_until")
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date discountedUntil;
	
	@JsonProperty("free_until")
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date freeUntil;
	
	@JsonProperty("enabled_at")
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date enabledAt;
	
	@JsonProperty("cancelled_at")
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date cancelledAt;
	
	@JsonProperty("external_property_id")
	private String externalPropertyId;

}
