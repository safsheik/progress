package com.progrexion.bcm.common.model.v1;

import java.util.List;

import lombok.Data;

@Data
public class TransactionResponseModel {
	private String status;
	private List<TransactionModel> transactions;

}
