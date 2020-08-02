package com.progrexion.bcm.common.model.vendor;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY) 
public class VendorOrdersResponseModel implements Serializable {

	private static final long serialVersionUID = 1L;

	@JsonProperty("id")
	private String orderId;

	@JsonProperty("url")
	private String url;

	@JsonProperty("payment_account")
	private VendorPaymentAccountResponseModel paymentAccountResponse;

	@JsonProperty("status")
	private String status;

	@JsonProperty("reference_id")
	private String referenceId;

	@JsonProperty("payment_source")
	private String paymentSource;
	

	@JsonProperty("type")
	private String type;

	@JsonProperty("rent")
	private float rent;

	@JsonProperty("other")
	private float other;

	@JsonProperty("total")
	private float total;

	@JsonProperty("fee")
	private float fee;

	@JsonProperty("paid_for")
	private String paidFor;

	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	@JsonProperty("created_at")
	private Date createdAt;

	@JsonProperty("reported_at")
	private VendorOrdersReportedResponseModel ordersReportResponse;

	@JsonProperty("check_number")
	private String checkNumber;

}
