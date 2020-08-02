package com.progrexion.bcm.common.model.v1;

import java.util.Date;

import lombok.Data;

@Data
public class LeaseOrdersResponseModel {

	private String orderId;
	private String url;
	private PaymentAccountResponseModel paymentAccountResponse;
	private String status;
	private String referenceId;
	private String paymentSource;
	private float rent;
	private String paidFor;
	private Date createdAt;

	private LeaseOrdersDetailsResponseModel ordersReportResponse;

	private String checkNumber;
}
