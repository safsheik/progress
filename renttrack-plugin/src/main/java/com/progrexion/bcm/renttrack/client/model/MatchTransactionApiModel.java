package com.progrexion.bcm.renttrack.client.model;

import java.io.Serializable;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
@Data

public class MatchTransactionApiModel implements Serializable {

	private static final long serialVersionUID = 1L;

	@JsonProperty("transaction_id")
	private String transactionID;

	@JsonProperty("amount")
	private String amount;

	@JsonProperty("name")
	private String name;

	@JsonProperty("category_id")
	private String categoryId;
	
	@JsonProperty("date")
	@JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDate date;

}
