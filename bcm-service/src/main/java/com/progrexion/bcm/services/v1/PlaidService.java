package com.progrexion.bcm.services.v1;

import com.progrexion.bcm.common.model.v1.PaymentAccountRequestModel;
import com.progrexion.bcm.common.model.v1.PaymentAccountResponseModel;
import  com.progrexion.bcm.common.model.v1.PlaidReconnectResponseModel;

public interface PlaidService {

	public PaymentAccountResponseModel createPlaidPaymentAccount(Long ucid, String brand, PaymentAccountRequestModel paymentAccountRequestModel) ;

	public PaymentAccountResponseModel getPaymentAccountDetails(Long ucid, String brand);

	public PlaidReconnectResponseModel reconnectPlaid(Long ucid, String brand, String reqType);

}