package com.progrexion.bcm.renttrack.client.utils;

import org.apache.commons.lang.StringUtils;

import com.progrexion.bcm.common.exceptions.BCMModuleException;
import com.progrexion.bcm.common.exceptions.BCMModuleExceptionEnum;
import com.progrexion.bcm.common.exceptions.BCMWebClientResponseException;
import com.progrexion.bcm.common.utils.CommonUtils;

public class RentTrackClientUtils {

	
	private RentTrackClientUtils() {
		//  Auto-generated constructor stub
	}

	public static BCMModuleException getException(String requestName, BCMWebClientResponseException wcEx)
	{
		String errorMessage = "";
		try {
		    errorMessage = CommonUtils.getErrorMessage(wcEx.getErrorMessage());
		} catch(Exception e) {
			errorMessage = StringUtils.isNotEmpty(wcEx.getErrorMessage())? "-"+wcEx.getErrorMessage():"";
		}
		BCMModuleExceptionEnum exceptionEnum = CommonUtils.getExceptionEnum(requestName);
		if(null == exceptionEnum) {
			exceptionEnum = BCMModuleExceptionEnum.REMOTE_SYSTEM_EXCEPTION;
		}
		BCMModuleException exception = new BCMModuleException(exceptionEnum);
		exception.setErrorMessage(exceptionEnum.getDescription().concat(" - "+errorMessage));
		return exception;

	}
	
	public static BCMModuleException getException(String requestName, BCMModuleException e)
	{
		
		String errorMessage =StringUtils.isNotBlank(e.getErrorMessage())? "-"+e.getErrorMessage() : "";
		BCMModuleExceptionEnum exceptionEnum =  CommonUtils.getExceptionEnum(requestName);
		if(null == exceptionEnum)
			exceptionEnum = BCMModuleExceptionEnum.REMOTE_SYSTEM_EXCEPTION;
		BCMModuleException exception = new BCMModuleException(exceptionEnum);
		exception.setErrorMessage(exceptionEnum.getDescription().concat(errorMessage));
		return exception;

	}

}
