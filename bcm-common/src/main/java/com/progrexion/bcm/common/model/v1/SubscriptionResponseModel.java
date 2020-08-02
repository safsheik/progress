package com.progrexion.bcm.common.model.v1;

import java.util.Date;

import lombok.Data;

@Data
public class SubscriptionResponseModel {

	private Long subscriptionId;
	private String url;
	private String paymentAccountUrl;
	private String planName;
	private String period;
	private String status;
	private Date lastPaymentDate;
	private Date nextPaymentDate;
	private Double nextPaymentAmount;
	private Date discountedUntil;
	private Date freeUntil;
	private Date enabledAt;
	private Date cancelledAt;
	private String externalPropertyId;


}
