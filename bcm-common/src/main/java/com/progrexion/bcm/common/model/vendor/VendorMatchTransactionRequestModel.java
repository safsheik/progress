package com.progrexion.bcm.common.model.vendor;

import java.time.LocalDate;

import lombok.Data;

@Data
public class VendorMatchTransactionRequestModel {

	private String transactionID;
	private String amount;
	private String name;
	private String categoryId;
	private LocalDate date;

}