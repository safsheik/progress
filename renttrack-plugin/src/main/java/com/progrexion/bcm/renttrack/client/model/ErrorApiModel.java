package com.progrexion.bcm.renttrack.client.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

@Data
@JsonInclude(Include.NON_NULL)
	public class ErrorApiModel implements Serializable{
		private static final long serialVersionUID = 1L;
		
		private String parameter;
		private String value;
		private String message;
}