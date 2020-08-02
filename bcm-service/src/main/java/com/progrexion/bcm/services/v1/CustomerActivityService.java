package com.progrexion.bcm.services.v1;

import com.progrexion.bcm.model.entities.CustomerActivity;

public interface CustomerActivityService {

	CustomerActivity saveCustomerActivity(CustomerActivity customerActivity);

	CustomerActivity getCustomerByCustActivityUsage(Long ucId, String brand, String custActivityUsage);

}