package com.progrexion.bcm.common.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@Data
@ConfigurationProperties(prefix = "renttrack.oauth")
public class RentTrackAPIOAuthConfigProperties {

	private String baseUrl;
	private String tokenUrl;
	private String clientId;
	private String clientSecret;
	private String username;
	private String password;
	private String grantType;
	private String refreshTokenUrl;
	
}
