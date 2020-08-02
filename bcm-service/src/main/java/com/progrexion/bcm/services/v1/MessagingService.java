package com.progrexion.bcm.services.v1;

import com.progrexion.bcm.common.model.v1.CancelSubscriptionMessageRequestModel;

public interface MessagingService {

	public Boolean processCancelSubscriptionMessage(CancelSubscriptionMessageRequestModel requestModel, String pbsMessage);

}
