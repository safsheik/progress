package com.progrexion.bcm.common.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@Data
@ConfigurationProperties(prefix = "renttrack.property")
public class RentTrackConfigProperties {
	private String subscriptionDetails;
	private String plaidPublicTokenPrefix;
	private float transactionAmountMinBy;
	private float transactionAmountMaxBy;
	private int transactionWindowOpenBy;
	private int transactionWindowCloseBy;
	private float trxByDefaultAmountMin;
	private float trxByDefaultAmountMax;
	private int trxByDefaultWindowOpen;
	private int trxByDefaultWindowClose;
	private String utilStatus;
	private String utilTypes;
	private String paymentAccPayType;
	

}
