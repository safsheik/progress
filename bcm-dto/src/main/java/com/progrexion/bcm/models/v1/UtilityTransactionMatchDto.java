package com.progrexion.bcm.models.v1;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY) 
@JsonIgnoreProperties(ignoreUnknown = true)
public class UtilityTransactionMatchDto implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@JsonProperty("util_transaction_id")
	private Long id;
	
//	@JsonProperty("url")
//	private String url;
	
	@JsonProperty("util_amount")
	private float amount;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	@JsonProperty("date")
	private Date date;
	
	

}
