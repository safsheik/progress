package com.progrexion.bcm.common.model.vendor;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY) 
@JsonIgnoreProperties(ignoreUnknown = true)
public class VendorOrdersReportedResponseModel implements Serializable {

	private static final long serialVersionUID = 1L;

	@JsonProperty("trans_union")
	private String transUnion;

	@JsonProperty("experian")
	private String experian;

	@JsonProperty("equifax")
	private String equifax;

}
