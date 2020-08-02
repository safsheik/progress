package com.progrexion.bcm.services.validator;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.progrexion.bcm.common.exceptions.BCMModuleException;
import com.progrexion.bcm.common.exceptions.BCMModuleExceptionEnum;
import com.progrexion.bcm.common.model.v1.UtilityStatusRequestModel;
import com.progrexion.bcm.common.properties.RentTrackConfigProperties;


@Component
public class UtilityValidator extends BCMValidator {
	
	@Autowired
	private RentTrackConfigProperties property;
	
	public void validateUtiltiStatusRequestModel(UtilityStatusRequestModel request) {
			if(!isValidStatus(request.getStatus()))
			{
				throw new BCMModuleException(BCMModuleExceptionEnum.PATCH_UTIL_INVALID_UTIL_STATUS);
			}
			
			if(!isValidUtility(request.getOptFor()))
			{
				throw new BCMModuleException(BCMModuleExceptionEnum.PATCH_UTIL_INVALID_UTIL_TYPE);
			}

	}
	
	private boolean isValidStatus(String status)
	{
		boolean isValid = false;
		String statusProperty = property.getUtilStatus();
		List<String> statusList= Arrays.asList(statusProperty.split(","));		
		if(statusList.contains(status))
		{
			isValid = true;
		}		
		return isValid;
	}
	
	private boolean isValidUtility(String utilityType)
	{
		boolean isValid = false;
		String utilTypes = property.getUtilTypes();
		List<String> utilList = Arrays.asList(utilTypes.split(","));
		if(utilList.contains(utilityType))
		{
			isValid = true;
		}		
		return isValid;
	}
}
