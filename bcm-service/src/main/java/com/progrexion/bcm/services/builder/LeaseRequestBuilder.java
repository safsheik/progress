package com.progrexion.bcm.services.builder;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.progrexion.bcm.common.model.v1.LeaseRequestModel;
import com.progrexion.bcm.common.model.vendor.VendorLeaseRequestModel;

@Component
public class LeaseRequestBuilder {

	public VendorLeaseRequestModel buildVendorLeaseRequestModel(LeaseRequestModel request) {
		return new ModelMapper().map(request, VendorLeaseRequestModel.class);
	}

}
