package com.progrexion.bcm.common.model.v1;

import java.util.Date;

import lombok.Data;

@Data

public class UtilityTransactionMatchModel {
	
	private Long id;
	private String url;
	private float amount;
	private Date date;

}
