package com.progrexion.bcm.common.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@Data
@ConfigurationProperties(prefix = "bcm.property")
public class BCMConfigProperties {
	private String logRequired;
	private String logOffMethods;	
	private int transactionPullCycleInDays;
}
