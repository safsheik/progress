package com.progrexion.bcm.models.v1;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL) 
@JsonIgnoreProperties(ignoreUnknown = true)
public class TransactionResponseDto implements Serializable{

	private static final long serialVersionUID = 1L;
	@JsonProperty("transaction_search_status")
	private String status;
	private List<TransactionDto> transactions;

}
