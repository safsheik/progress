package com.progrexion.bcm.common.model.vendor;

import lombok.Data;

@Data
public class VendorTransactionRequestModel {

	private float rentAmount;
	private int dueDay;
	private String paymentAccountUrl;
	private String leaseUrl;
	private Long transactionFinderId;
	private Long leaseId;

}