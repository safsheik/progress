package com.progrexion.bcm.common.model.vendor;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

@Data
@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class VendorTransactionResponseModel implements Serializable {

	private static final long serialVersionUID = 1L;

	@JsonProperty("id")
	private Long id;
	@JsonProperty("url")
	private String url;
	@JsonProperty("status")
	private String status;

}
