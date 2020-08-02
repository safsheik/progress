package com.progrexion.bcm.common.model.v1;

import lombok.Data;

@Data
public class UtilityMatchSummaryModel {
	private String count;
	private UtilityTransactionMatchModel newest;
	private UtilityTransactionMatchModel oldest;
	
	

}
