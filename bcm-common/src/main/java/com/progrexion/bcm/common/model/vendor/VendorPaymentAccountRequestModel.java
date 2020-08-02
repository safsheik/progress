package com.progrexion.bcm.common.model.vendor;

import lombok.Data;

@Data
public class VendorPaymentAccountRequestModel {

	private String publicToken;
	private String contractUrl;
	private String depositAccountUrl;
	private String payType;
	private String plaidAccountId;

}
