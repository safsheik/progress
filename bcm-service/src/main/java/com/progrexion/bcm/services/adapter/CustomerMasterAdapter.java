package com.progrexion.bcm.services.adapter;

import java.util.Set;


public interface CustomerMasterAdapter {


	public Set<Long> getCustomerUCIDs(Long ucid);

	Long getCustomerParentUCID(Long ucid);


}
