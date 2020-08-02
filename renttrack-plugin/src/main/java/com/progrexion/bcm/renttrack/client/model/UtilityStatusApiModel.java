package com.progrexion.bcm.renttrack.client.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
@Data
@JsonInclude(Include.NON_NULL)
public class UtilityStatusApiModel implements Serializable {

	private static final long serialVersionUID = 1L;

	@JsonProperty("status")
	private String status;

}
