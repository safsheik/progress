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

public class TransactionDto implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@JsonProperty("transaction_id")
	private String transactionId;
	
	@JsonProperty("transaction_amount")
	private float amount;
	
	@JsonProperty("name")
	private String name;
//
//	@JsonProperty("category_id")
//	private String categoryId;
	
	@JsonProperty("transaction_date")
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date dueDay;

}
