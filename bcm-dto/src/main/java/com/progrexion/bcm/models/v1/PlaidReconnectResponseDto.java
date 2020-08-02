package com.progrexion.bcm.models.v1;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
@JsonInclude(JsonInclude.Include.NON_NULL) 
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PlaidReconnectResponseDto {

	@JsonProperty("public_token")
	private String	publicToken;
	@JsonFormat(pattern = "YYYY-MM-DDThh:mm:ss")
	@JsonProperty("expiration")
	private String expiration;


}
