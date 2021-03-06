package com.progrexion.bcm.common.model.vendor;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

@Data
@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class VendorSearchTransactionResponseModel implements Serializable {

	private static final long serialVersionUID = 1L;

	@JsonProperty("transaction_id")
	private String transactionId;
	
	@JsonProperty("amount")
	private float amount;
	
	@JsonProperty("name")
	private String name;

	@JsonProperty("category_id")
	private String categoryId;
	
	@JsonProperty("date")
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date dueDay;
	
}
