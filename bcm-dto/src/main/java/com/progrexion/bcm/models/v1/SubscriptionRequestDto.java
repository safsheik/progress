package com.progrexion.bcm.models.v1;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;


@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SubscriptionRequestDto implements Serializable{
	private static final long serialVersionUID = 1L;
	private String vendor;
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
