package com.progrexion.bcm.services.builder;

import org.springframework.stereotype.Component;

import com.progrexion.bcm.common.model.v1.UtilityStatusRequestModel;
import com.progrexion.bcm.common.model.vendor.VendorUtilityStatusRequestModel;

@Component
public class UtilityRequestBuilder {

	public VendorUtilityStatusRequestModel buildUtiltyStatusRequestModel(UtilityStatusRequestModel request) {
		VendorUtilityStatusRequestModel requestModel = new VendorUtilityStatusRequestModel();
		requestModel.setUtilityId(request.getUtilityId());
		requestModel.setStatus(request.getStatus());
		return requestModel;
	}


}
