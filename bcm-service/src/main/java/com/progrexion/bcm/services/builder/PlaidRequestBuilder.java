package com.progrexion.bcm.services.builder;

import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.progrexion.bcm.common.model.v1.PaymentAccountRequestModel;
import com.progrexion.bcm.common.model.vendor.VendorPaymentAccountRequestModel;
import com.progrexion.bcm.common.properties.RentTrackConfigProperties;

@Component
public class PlaidRequestBuilder {
	@Autowired
	private RentTrackConfigProperties property;
	
	public VendorPaymentAccountRequestModel buildPlaidVendorPaymentAccountRequestModel(PaymentAccountRequestModel request) {
		if(StringUtils.isBlank(request.getPayType()))
		{
			request.setPayType(property.getPaymentAccPayType());
		}
		return  new ModelMapper().map(request, VendorPaymentAccountRequestModel.class);
	}


}
