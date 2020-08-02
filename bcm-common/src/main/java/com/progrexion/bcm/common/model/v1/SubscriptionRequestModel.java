package com.progrexion.bcm.common.model.v1;


import lombok.Data;

@Data
public class SubscriptionRequestModel {


	private String paymentAccountUrl;
	private String plan;
	private String period;
	private String uuId;
	private String ucId;
	private String promotionCode;
	private boolean uuidRequired;
	private String reasonCode;
	private String reasonMessage;

}
