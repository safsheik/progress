package com.progrexion.bcm.services.factory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.progrexion.bcm.common.processor.VendorProcessor;
import com.progrexion.bcm.messagingconfig.enums.BCMVendorEnum;

@Component
public class VendorProcessorFactory {

	@Autowired
	@Qualifier("renttrackprocessor")
	private VendorProcessor renttrackprocessor;

	/**
	 * This method will determine the VendorProcessor based on the BCMVendorEnum
	 * 
	 * @param vendors
	 * @return
	 */
	public VendorProcessor getVendorProcessor(BCMVendorEnum vendors) {
		VendorProcessor vendorProcessor = null;
		if(vendors.equals(BCMVendorEnum.RENTTRACK)) {
			vendorProcessor = renttrackprocessor;
		}		
		return vendorProcessor;
	}
}
