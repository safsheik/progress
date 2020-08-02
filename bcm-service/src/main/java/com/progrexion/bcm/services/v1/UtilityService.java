package com.progrexion.bcm.services.v1;

import java.util.List;

import com.progrexion.bcm.common.model.v1.UtilityDetailsResponseModel;
import com.progrexion.bcm.common.model.v1.UtilityResponseModel;
import com.progrexion.bcm.common.model.v1.UtilityStatusRequestModel;
import com.progrexion.bcm.common.model.vendor.VendorOrdersResponseModel;
import com.progrexion.bcm.model.entities.BCMCustomer;

public interface UtilityService {

	UtilityResponseModel[] getAllUtilities(Long ucid, String brand);
	
	UtilityDetailsResponseModel[] getUtilityDetails(Long ucid, String brand,
			Long utilityTradeLineId);

	boolean updateUtiltyStatus(Long ucid, String brand, UtilityStatusRequestModel request);

	List<VendorOrdersResponseModel> getUtilityOrdersByBCMCustomer(BCMCustomer bcmCustomer);

	
}