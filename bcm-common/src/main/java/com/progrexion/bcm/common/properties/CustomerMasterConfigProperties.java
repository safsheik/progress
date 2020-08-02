package com.progrexion.bcm.common.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@Data
@ConfigurationProperties(prefix = "customermaster.property")
public class CustomerMasterConfigProperties {
	private String apiUser;
	private String password;
	private String host;
	private String customerDetailsUri;
	private String headerNameEndUser;
	private String headerValueEndUser;
	private String headerNameEndUserType;
	private String headerValueEndUserType;
	private String customerUri;
	private int apiRetryCount;
	
}
