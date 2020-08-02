package com.progrexion.bcm.services.util;

import com.progrexion.bcm.common.model.WebServiceLogsModel;
import com.progrexion.bcm.services.constants.TestConstants;



public class DataBuilderUtil {


	public static WebServiceLogsModel buildWebServiceLogsModel() {
		WebServiceLogsModel webServiceLog = new WebServiceLogsModel();
		webServiceLog.setUcid(TestConstants.UCID);
		//webServiceLog.setBrand(TestConstants.BRAND_CCOM);
		webServiceLog.setHttpMethod(TestConstants.METHOD);
		webServiceLog.setHttStatusCode(200);
		webServiceLog.setResponseBody(TestConstants.FAILED_MESSAGE_MESSAGE_BODY);
		webServiceLog.setRequestBody(null);
		webServiceLog.setUrl("/test/");
		return webServiceLog;
	}




}
