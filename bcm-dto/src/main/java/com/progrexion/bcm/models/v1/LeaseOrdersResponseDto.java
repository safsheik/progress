package com.progrexion.bcm.models.v1;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL) 
@JsonIgnoreProperties(ignoreUnknown = true)
public class LeaseOrdersResponseDto implements Serializable {

	private static final long serialVersionUID = 1L;

	@JsonProperty("id")
	private String orderId;
	@JsonProperty("payment_account")
	private PaymentAccountResponseDto paymentAccountResponse;
	private String status;
	private float rent;
	@JsonProperty("reference_id")
	private String referenceId;
	@JsonProperty("payment_source")
	private String paymentSource;
	@JsonProperty("paid_for")
	private String paidFor;
	@JsonProperty("created_at")
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date createdAt;
	@JsonProperty("reported_at")
	private LeaseOrdersDetailsResponseDto ordersReportResponse;

	private String checkNumber;

}
