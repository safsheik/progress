package com.progrexion.bcm.services.handler;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class VendorAPINotificationHandlerTest {

	@Mock
	private VendorAPILogHandler logHandler;

	@InjectMocks
	public VendorAPINotificationHandler vendorAPINotificationHandler;

	@Test
	public void testHandle() {
		String url = "test.com";
		String request = "request";
		String response = "response";
		vendorAPINotificationHandler.notify(url, request, response,"POST");
		assertNotNull(vendorAPINotificationHandler);
	}
	
	@Test
	public void testAddActivity() {
		vendorAPINotificationHandler.addActivity("TEST_ACTIVITY");
		assertNotNull(vendorAPINotificationHandler);
	}

}
