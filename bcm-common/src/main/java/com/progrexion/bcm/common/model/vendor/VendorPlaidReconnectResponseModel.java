package com.progrexion.bcm.common.model.vendor;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY) 
@JsonIgnoreProperties(ignoreUnknown = true)
public class VendorPlaidReconnectResponseModel implements Serializable {

	private static final long serialVersionUID = 1L;	
	
	@JsonProperty("public_token")
	private String	publicToken;
	@JsonFormat(pattern = "YYYY-MM-DDThh:mm:ss")
	@JsonProperty("expiration")
	private String expiration;

}
