package com.progrexion.bcm.common.model.v1;

import java.util.List;

import lombok.Data;

@Data
public class PaymentAccountResponseModel {

	private Long id;
	private String	url;
	private String	nickName;
	private String	type;
	private String	lastFour;
	private String	expiration;
	private String	institutionId;
	private boolean	plaidAccount;
	private boolean	needsReconnect;
	private boolean	historicalUpdateComplete;
	private String	cardBrand;
	
	private String status;
	private List<TransactionModel> transactions;


}
