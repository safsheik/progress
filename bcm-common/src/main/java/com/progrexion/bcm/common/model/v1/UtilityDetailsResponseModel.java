package com.progrexion.bcm.common.model.v1;

import java.util.Date;

import lombok.Data;

@Data
public class UtilityDetailsResponseModel {
	private Long id;
	private String url;
	private String name;
	private float amount;
	private Date date;
	private String paymentAccountUrl;
	

}
