package com.progrexion.bcm.services.v1;

import com.progrexion.bcm.common.model.v1.MatchTransactionRequestModel;
import com.progrexion.bcm.common.model.v1.MatchTransactionResponseModel;
import com.progrexion.bcm.common.model.v1.TransactionRequestModel;
import com.progrexion.bcm.common.model.v1.TransactionResponseModel;
import com.progrexion.bcm.common.model.vendor.VendorTransactionResponseModel;
import com.progrexion.bcm.model.entities.BCMCustomer;

public interface TransactionService {

	public TransactionResponseModel searchTransactionDetails(Long ucid, String brand);

	public TransactionResponseModel createTransactionFinder(Long ucid,String brand,
			TransactionRequestModel transactionRequestModel);
	
	public MatchTransactionResponseModel matchTransaction(Long ucid,String brand,
			MatchTransactionRequestModel matchTransactionRequestModel);
	
	public TransactionResponseModel createOrUpdateTransactionFinder(BCMCustomer customer,
			TransactionRequestModel transactionRequestModel);

	public VendorTransactionResponseModel createTransactionFinder(BCMCustomer customer,
			TransactionRequestModel transactionRequestModel);



}
