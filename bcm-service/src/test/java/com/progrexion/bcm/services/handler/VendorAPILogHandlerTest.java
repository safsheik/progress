package com.progrexion.bcm.services.handler;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.progrexion.bcm.model.entities.BCMCustomer;
import com.progrexion.bcm.model.entities.ExternalLog;
import com.progrexion.bcm.services.builder.EntityDataBuilder;
import com.progrexion.bcm.services.persistence.PersistenceService;
import com.progrexion.bcm.services.util.TestServiceUtils;
import com.progrexion.bcm.services.v1.CustomerActivityService;
import com.progrexion.bcm.model.entities.CustomerActivity;

@RunWith(MockitoJUnitRunner.class)
public class VendorAPILogHandlerTest {

	@Mock
	private EntityDataBuilder entityDataBuilder;

	@Mock
	private PersistenceService persistenceService;
	@Mock
	private CustomerActivityService customerActivityService;

	@InjectMocks
	public VendorAPILogHandler vendorAPILogHandler;

	@Test
	public void testHandle() {
		Long vendorId = 1L;
		String url = "test.com";
		String request = "request";
		String response = "response";
		BCMCustomer customer =new BCMCustomer();
		customer.setUcId(10L);
		customer.setBrand("CCOM");
		when(entityDataBuilder.buildExternalLogs(customer, vendorId, url, request, response,"POST"))
				.thenCallRealMethod();
		Mockito.doNothing().when(persistenceService).saveExternalLogs(Mockito.any(ExternalLog.class));
		vendorAPILogHandler.handle(customer, vendorId, url, request, response, "POST");
		assertNotNull(vendorAPILogHandler);
	}
	
	@Test
	public void testAddActivity() {
		BCMCustomer customer =TestServiceUtils.getCustomerObject();

		when(entityDataBuilder.buildCustomerActivity(customer,"CREATE_TENANT"))
				.thenCallRealMethod();
		when(customerActivityService.saveCustomerActivity(Mockito.any(CustomerActivity.class))).thenReturn(new CustomerActivity());
		vendorAPILogHandler.addActivity(customer, "CREATE_TENANT");
		assertNotNull(vendorAPILogHandler);
	}

}
