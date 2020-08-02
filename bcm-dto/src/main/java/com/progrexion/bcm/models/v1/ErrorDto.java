package com.progrexion.bcm.models.v1;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ErrorDto {

	private boolean status;
	private int statusCode;
	private String error;
	private String errorCode;
	
//    "status": false,
//    "statusCode": 400,
//    "error": "Build Credit Module Exception: Error validating data. Please check parameters and retry",
//    "errorCode": "BCM-400"

	public ErrorDto() {
		super();
	}

	public ErrorDto(boolean status, int statusCode, String error, String errorCode) {
		super();
		this.status = status;
		this.statusCode = statusCode;
		this.error = error;
		this.errorCode = errorCode;
	}


	

}
