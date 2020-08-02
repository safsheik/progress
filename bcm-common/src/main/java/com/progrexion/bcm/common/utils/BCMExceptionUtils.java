package com.progrexion.bcm.common.utils;

import org.apache.commons.lang.StringUtils;
import org.springframework.http.HttpStatus;

import com.progrexion.bcm.common.exceptions.BCMModuleException;
import com.progrexion.bcm.common.exceptions.BCMModuleExceptionEnum;
import com.progrexion.bcm.common.exceptions.BCMWebClientResponseException;

import lombok.extern.slf4j.Slf4j;
@Slf4j
public class BCMExceptionUtils {


	private static final String BCM_INTERNAL_SERVER_ERROR_SUFFIX = "_500_BCM";
	
	private BCMExceptionUtils() {
		
	}
	
	public static BCMModuleException generateBCMModuleGeneralException(Exception ex) {
		BCMModuleException exception = new BCMModuleException();
		exception.setErrorMessage(ex.getMessage());
		return exception;
	}
	
	public static Boolean isTokenExpired(BCMWebClientResponseException wcEx) {
		if (wcEx.getHttpStatus().value() == 401 && wcEx.getErrorMessage().contains("The access token provided has expired")) {
			log.error("RentTrack: Unauthorized access error (401) occurred. The access token provided has expired.");
			return true;
		} else {
			return false;
		}
	}
	
	public static BCMModuleException getBCMModuleExceptionMessage(String customException, Exception ex) {
		String errorMessage = "";
		HttpStatus httpStatus = null;
		if (ex instanceof BCMWebClientResponseException) {
			BCMWebClientResponseException wcEx = null;
			try {
				wcEx = (BCMWebClientResponseException) ex;
				httpStatus = wcEx.getHttpStatus();
				customException = customException + "_" + wcEx.getHttpStatus().value();

				errorMessage = CommonUtils.getErrorMessage(wcEx.getErrorMessage());

			} catch (Exception e) {
				errorMessage = StringUtils.isNotEmpty(wcEx.getErrorMessage()) ? " : " + wcEx.getErrorMessage() : "";
			}
		} else {
			httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
			customException = customException + BCM_INTERNAL_SERVER_ERROR_SUFFIX;
			errorMessage = ex.getMessage();
		}
		
		BCMModuleException exception = new BCMModuleException(getBCMModuleExceptionEnum(customException, httpStatus));
		exception.setErrorMessage(errorMessage);
		return exception;
	}
	
	private static BCMModuleExceptionEnum getBCMModuleExceptionEnum(String customException, HttpStatus httpStatus) {
		BCMModuleExceptionEnum exceptionEnum = BCMModuleExceptionEnum.valueOf(customException);
		if (null == exceptionEnum) {
			if (null != httpStatus) {
				if (httpStatus.value() == 401 || httpStatus.value() == 403) {
					exceptionEnum = BCMModuleExceptionEnum.UNAUTHORIZED_REMOTE_SYSTEM_EXCEPTION;
				} else if (httpStatus.is4xxClientError()) {
					exceptionEnum = BCMModuleExceptionEnum.INVALID_INPUT_REMOTE_SYSTEM_EXCEPTION;
				} else if (httpStatus.is5xxServerError()) {
					exceptionEnum = BCMModuleExceptionEnum.UNKNOWN_REMOTE_SYSTEM_EXCEPTION;
				} else {
					exceptionEnum = BCMModuleExceptionEnum.REMOTE_SYSTEM_EXCEPTION;
				}
			} else {
				exceptionEnum = BCMModuleExceptionEnum.REMOTE_SYSTEM_EXCEPTION;
			}
		}
		return exceptionEnum;
	}
}
