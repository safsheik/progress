package com.progrexion.bcm.common.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.progrexion.bcm.common.AppConstants;
import com.progrexion.bcm.common.properties.BCMConfigProperties;
@Component
public class BCMLoadPropOnStartUp {
	
	private List<String> methodTypeList = new ArrayList<>();
	
	private boolean isLogOn;
	
	@Autowired
	private BCMConfigProperties property;
	
@EventListener(ApplicationReadyEvent.class)
	public void loadBCMConfigProperties() {
		if(property.getLogRequired().equals(AppConstants.ON))
		{
			isLogOn = true;
			String logOffMethods= property.getLogOffMethods();
			if(StringUtils.isNotBlank(logOffMethods) && logOffMethods.contains(","))
			{
				methodTypeList = Arrays.asList(logOffMethods.split(","));	
									
			}
			else {
				methodTypeList.add(logOffMethods);
			}

		}
	}

public List<String> getMethodTypeList() {
	return methodTypeList;
}

public boolean isLogOn() {
	return isLogOn;
}


}