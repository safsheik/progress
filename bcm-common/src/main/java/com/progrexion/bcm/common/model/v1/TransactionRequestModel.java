package com.progrexion.bcm.common.model.v1;

import lombok.Data;

@Data
public class TransactionRequestModel {

	private float rentAmount;
	private int dueDay;
	private String leaseUrl;
	private String paymentAccountUrl;
	private Long transactionFinderId;
	private Long leaseId;

}
