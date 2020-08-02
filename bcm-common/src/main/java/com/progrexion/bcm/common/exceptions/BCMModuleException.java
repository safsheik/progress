package com.progrexion.bcm.common.exceptions;

public class BCMModuleException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	private String errorCode;
	private String errorMessage;
	private String errorData;
	private String requestData;
	private String responseData;
	private Integer httpStatusCode;

	public BCMModuleException() {
		this.errorCode = BCMModuleExceptionEnum.BCM_MODULE_EXCEPTION.getCode();
		this.errorMessage = BCMModuleExceptionEnum.BCM_MODULE_EXCEPTION.getDescription();
		this.httpStatusCode = BCMModuleExceptionEnum.BCM_MODULE_EXCEPTION.getHttpStatusCode();
		this.errorData = BCMModuleExceptionEnum.BCM_MODULE_EXCEPTION.getDescription();

	}

	public BCMModuleException(String errorMessage, BCMModuleExceptionEnum bcmModuleExceptionEnum) {
		this.errorMessage = bcmModuleExceptionEnum.getDescription();
		this.errorCode = bcmModuleExceptionEnum.getCode();
		this.httpStatusCode = bcmModuleExceptionEnum.getHttpStatusCode();
		this.errorData = errorMessage;
	}

	public BCMModuleException(BCMModuleExceptionEnum bcmModuleExceptionEnum) {
		this.errorMessage = bcmModuleExceptionEnum.getDescription();
		this.errorCode = bcmModuleExceptionEnum.getCode();
		this.httpStatusCode = bcmModuleExceptionEnum.getHttpStatusCode();
		this.errorData = bcmModuleExceptionEnum.getDescription();
	}

	public BCMModuleException(String errorMessage, String errorData,
			BCMModuleExceptionEnum bcmModuleExceptionEnum) {
		this.errorMessage = errorMessage;
		this.errorCode = bcmModuleExceptionEnum.getCode();
		this.httpStatusCode = bcmModuleExceptionEnum.getHttpStatusCode();
		this.errorData = errorData;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public BCMModuleException(String errorCode, String errorMessage, String errorData, Integer httpStatusCode) {
		super();
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
		this.errorData = errorData;
		this.httpStatusCode = httpStatusCode;
	}
	public BCMModuleException(String errorCode, String errorMessage, Integer httpStatusCode) {
		super();
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
		this.httpStatusCode = httpStatusCode;
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

	public String getRequestData() {
		return requestData;
	}

	public void setRequestData(String requestData) {
		this.requestData = requestData;
	}

	public String getResponseData() {
		return responseData;
	}

	public void setResponseData(String responseData) {
		this.responseData = responseData;
	}

	public Integer getHttpStatusCode() {
		return httpStatusCode;
	}

	public void setHttpStatusCode(Integer httpStatusCode) {
		this.httpStatusCode = httpStatusCode;
	}

	public String getErrorData() {
		return errorData;
	}

	public void setErrorData(String errorData) {
		this.errorData = errorData;
	}

	@Override
	public String toString() {
		return "BCMModuleException [errorCode=" + errorCode + ", errorMessage=" + errorMessage + ", errorData="
				+ errorData + ", requestData=" + requestData + ", responseData=" + responseData + ", httpStatusCode="
				+ httpStatusCode + "]";
	}

}
