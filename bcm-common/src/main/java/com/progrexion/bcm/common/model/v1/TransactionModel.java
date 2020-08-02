package com.progrexion.bcm.common.model.v1;

import java.util.Date;
import lombok.Data;

@Data
public class TransactionModel {
	
	private String transactionId;

	private float amount;

	private String name;

	private String categoryId;

	private Date dueDay;

}
