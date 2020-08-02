package com.progrexion.bcm.common.model.v1;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class MatchTransactionRequestModel {

	private String transactionID;
	private String amount;
	private String name;
	private String categoryId;

	@JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDate date;

}
