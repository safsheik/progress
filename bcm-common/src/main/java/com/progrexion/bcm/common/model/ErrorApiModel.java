package com.progrexion.bcm.common.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Data
@JsonInclude(Include.NON_NULL)
	public class ErrorApiModel implements Serializable{
		private static final long serialVersionUID = 1L;
		@JsonProperty("parameter")
		private String parameter;
		@JsonProperty("value")
		private String value;
		@JsonProperty("message")
		private String message;
}