package com.progrexion.bcm.models.v1;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL) 
@JsonIgnoreProperties(ignoreUnknown = true)
public class LeaseStatusResponseDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private Boolean updateStatus;

}
