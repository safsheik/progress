package com.progrexion.bcm.common.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.WebClientResponseException;

public class BCMWebClientResponseException extends WebClientResponseException {

	private static final long serialVersionUID = 1L;

	private BCMModuleExceptionEnum bcmModuleExceptionEnum;
	private String errorMessage;
	private HttpStatus httpStatus;

	public BCMWebClientResponseException(String errorMessage, HttpStatus httpStatus,
			BCMModuleExceptionEnum bcmModuleExceptionEnum) {
		super(httpStatus.ordinal(), errorMessage, null, null, null);
		this.errorMessage = errorMessage;
		this.httpStatus = httpStatus;
		this.bcmModuleExceptionEnum = bcmModuleExceptionEnum;

	}

	public BCMModuleExceptionEnum getBcmModuleExceptionEnum() {
		return bcmModuleExceptionEnum;
	}

	public void setBcmModuleExceptionEnum(BCMModuleExceptionEnum bcmModuleExceptionEnum) {
		this.bcmModuleExceptionEnum = bcmModuleExceptionEnum;
	}


	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public HttpStatus getHttpStatus() {
		return httpStatus;
	}

	public void setHttpStatus(HttpStatus httpStatus) {
		this.httpStatus = httpStatus;
	}

	@Override
	public String toString() {
		return "BCMWebClientResponseException [bcmModuleExceptionEnum=" + bcmModuleExceptionEnum
				+ ", errorMessage=" + errorMessage + ", httpStatus=" + httpStatus + "]";
	}

}
