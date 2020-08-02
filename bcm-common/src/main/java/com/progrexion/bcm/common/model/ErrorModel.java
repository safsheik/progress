package com.progrexion.bcm.common.model;

public class ErrorModel {

	private String errorCode;
	private String errorMessage;
	private String errorData;
	private Integer httpStatusCode;

	public ErrorModel() {

	}

	public ErrorModel(String errorCode, String errorMessage, String errorData, Integer httpStatusCode) {
		super();
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
		this.errorData = errorData;
		this.httpStatusCode = httpStatusCode;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getErrorData() {
		return errorData;
	}

	public void setErrorData(String errorData) {
		this.errorData = errorData;
	}

	public Integer getHttpStatusCode() {
		return httpStatusCode;
	}

	public void setHttpStatusCode(Integer httpStatusCode) {
		this.httpStatusCode = httpStatusCode;
	}

	@Override
	public String toString() {
		return "ErrorModel [errorCode=" + errorCode + ", errorMessage=" + errorMessage + ", errorData=" + errorData
				+ ", httpStatusCode=" + httpStatusCode + "]";
	}

}
