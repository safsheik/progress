package com.progrexion.bcm.services.v1;

import com.progrexion.bcm.common.model.v1.CreateUserRequestModel;
import com.progrexion.bcm.common.model.v1.CreateUserResponseModel;
import com.progrexion.bcm.common.model.v1.CustomerStatusResponseModel;
import com.progrexion.bcm.model.entities.BCMCustomer;

public interface CustomerService {

	BCMCustomer createBcmCustomer(BCMCustomer customer); 

	BCMCustomer updateBcmCustomer(BCMCustomer customer);

	BCMCustomer getCustomerById(Long customerId);
	
	BCMCustomer getCustomerByUcId(Long ucId);

	CreateUserResponseModel createUserAccount(Long ucid, String brand, CreateUserRequestModel reqModel);
	
	BCMCustomer getCustomerByEmail(String custEmail);
	
	BCMCustomer getCustomerByUcIdAndBrand(Long ucId,String brand);
	
	Long getTransactionFinderId(Long ucId,String brand);
	
	CustomerStatusResponseModel getCustomerStatusInfo(Long ucid, String brand);
	
}