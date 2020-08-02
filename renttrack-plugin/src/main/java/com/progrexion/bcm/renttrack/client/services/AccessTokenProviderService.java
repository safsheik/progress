package com.progrexion.bcm.renttrack.client.services;

import com.progrexion.bcm.common.model.vendor.VendorTokenResponseModel;
import com.progrexion.bcm.common.model.vendor.enums.RentTrackClientRequestTypesEnum;

public interface AccessTokenProviderService {

	VendorTokenResponseModel getFirstTimeUserTokens(String username, String password);

	VendorTokenResponseModel getCurrentTokensOfUser(Long ucid,String brand);

	VendorTokenResponseModel refreshTokensOfUser(Long ucid,String brand,String reqType);

	VendorTokenResponseModel getPartnerTokens();
	
	String getPartnerAccessToken();

	String refreshAndGetAccessTokenOfUser(Long ucid,String brand);

	String getCurrentAccessTokenOfUser(Long ucid,String brand);
	
	String getCurrentAccessTokenOfUser(Long customerId);

	VendorTokenResponseModel getCurrentTokensOfUser(Long customerId);
	
	VendorTokenResponseModel refreshTokensOfUser(Long customerId,RentTrackClientRequestTypesEnum reqType);
}
