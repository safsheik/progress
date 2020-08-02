package com.progrexion.bcm.services.v1;

import java.util.List;

import com.progrexion.bcm.common.model.v1.LeaseOrdersResponseModel;
import com.progrexion.bcm.common.model.v1.LeaseRequestModel;
import com.progrexion.bcm.common.model.v1.LeaseResponseModel;
import com.progrexion.bcm.common.model.vendor.VendorOrdersResponseModel;
import com.progrexion.bcm.model.entities.BCMCustomer;

public interface LeaseService {

	public boolean createLease(BCMCustomer bcmCustomer, LeaseRequestModel leaseRequestModel) ;
	
	public LeaseResponseModel createOrUpdateLease(Long ucid, String brand, LeaseRequestModel leaseRequestModel, boolean isSubscription) ;

	public LeaseResponseModel getLeaseInfo(Long ucid, String brand) ;
		
	public LeaseOrdersResponseModel[] getLeaseOrders(Long ucid, String brand) ;

	public boolean getLeaseInfo(BCMCustomer bcmCustomer);

	public List<VendorOrdersResponseModel> getLeaseOrdersByBCMCustomer(BCMCustomer bcmCustomer);

}
