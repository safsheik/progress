package com.progrexion.bcm.common.model.v1;

import lombok.Data;

@Data
public class MatchTransactionResponseModel {

	private Long id;
	private String status;
	private String referenceId;
	private String paymentSource;
	private String type;
	private String message;
	private String paidFor;
	private String depositedAt;
	private String reportedAt;
	private String deliveryMethod;

}
