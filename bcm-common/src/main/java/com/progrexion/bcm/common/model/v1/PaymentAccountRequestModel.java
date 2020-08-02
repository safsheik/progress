package com.progrexion.bcm.common.model.v1;

import lombok.Data;

@Data
public class PaymentAccountRequestModel {


	private String publicToken;
	private String contractUrl;
	private String depositAccountUrl;
	private String payType;
	private String plaidAccountId;

}
