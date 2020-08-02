package com.progrexion.bcm.services.factory;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.progrexion.bcm.common.processor.VendorProcessor;
import com.progrexion.bcm.messagingconfig.enums.BCMVendorEnum;

@RunWith(MockitoJUnitRunner.class)
public class VendorProcessorFactoryTest {

	@Mock
	private VendorProcessor renttrackprocessor;
	@InjectMocks
	public VendorProcessorFactory mockVendorProcessorFactory;
	@Test
	public void test_getVendorProcessorForRenttrack() {
		BCMVendorEnum vendors = BCMVendorEnum.RENTTRACK;
		VendorProcessor response = mockVendorProcessorFactory.getVendorProcessor(vendors);
		assertNotNull(response);
	}

}
