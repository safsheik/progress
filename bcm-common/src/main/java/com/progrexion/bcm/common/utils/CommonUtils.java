package com.progrexion.bcm.common.utils;

import java.util.Arrays;
import java.util.Base64;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import com.progrexion.bcm.common.AppConstants;
import com.progrexion.bcm.common.exceptions.BCMModuleExceptionEnum;

public class CommonUtils {

	/**
	 * This method is used to generate the Authorization string value based on the
	 * given input
	 * @param username
	 * @param password
	 * @return Authorization value
	 */
	private CommonUtils() {
		
	}
	public static String setToken(String token) {	
		return "Bearer ".concat(token);
	}

	public static Map<String, String> getHashMapFromString(String value)
	{
		return Arrays.stream(value.split(","))
	            .map(s -> s.split(":"))
	            .collect(Collectors.toMap(s -> s[0], s -> s[1]));
		
	}
	
	public static BCMModuleExceptionEnum getExceptionEnum(String enumName)
	{
		BCMModuleExceptionEnum[] enumArray = BCMModuleExceptionEnum.values();
		for(BCMModuleExceptionEnum enumObj:enumArray)
		{
			if(enumObj.name().equals(enumName))
			{
				return enumObj;
			}
		}
		return null;
	}

	public static String getBasicAuthCred(String username, String password) {
		String creds = username + ":" + password;
		byte[] plainCredsBytes = creds.getBytes();
		return "Basic ".concat(Base64.getEncoder().encodeToString(plainCredsBytes));
	}	
	
	public static String getErrorMessage(String errorMsg) {
		String errorMessage = "";
		errorMsg = errorMsg.replaceAll(AppConstants.WEBCLIENT_MSG_REGEX,"");
		Map<String, String> map = getHashMapFromString(errorMsg);
		if(map.containsKey(AppConstants.WEBCLIENT_MSG)) {
			errorMessage = map.get(AppConstants.WEBCLIENT_MSG);
		}
		if(map.containsKey(AppConstants.WEBCLIENT_ERROR_DESC)) {
			errorMessage = map.get(AppConstants.WEBCLIENT_ERROR_DESC);
		}
		return StringUtils.isNotEmpty(errorMessage)? errorMessage.replaceAll("\"", "") : "";
	}

}