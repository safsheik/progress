package com.progrexion.bcm.services.builder;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.progrexion.bcm.common.model.v1.MatchTransactionRequestModel;
import com.progrexion.bcm.common.model.v1.TransactionRequestModel;
import com.progrexion.bcm.common.model.vendor.VendorMatchTransactionRequestModel;
import com.progrexion.bcm.common.model.vendor.VendorTransactionRequestModel;

@Component
public class TransactionRequestBuilder {

	public VendorTransactionRequestModel buildVendorTransactionFinderRequestModel(TransactionRequestModel request) {
		return new ModelMapper().map(request, VendorTransactionRequestModel.class);

	}

	public VendorMatchTransactionRequestModel buildVendorMatchTransactionRequestModel(MatchTransactionRequestModel request) {
		return new ModelMapper().map(request, VendorMatchTransactionRequestModel.class);
	}
}
