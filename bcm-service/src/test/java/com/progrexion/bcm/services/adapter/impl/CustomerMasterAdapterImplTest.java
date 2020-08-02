package com.progrexion.bcm.services.adapter.impl;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.progrexion.bcm.common.exceptions.BCMModuleException;
import com.progrexion.bcm.common.exceptions.BCMModuleExceptionEnum;
import com.progrexion.bcm.common.properties.CustomerMasterConfigProperties;
import com.progrexion.bcm.common.webclients.WebClientConfiguration;
import com.progrexion.bcm.common.webclients.errorhandlers.WebClientErrorHandler;
import com.progrexion.bcm.common.webclients.utils.WebClientHttpRequestHeader;
import com.progrexion.bcm.services.constants.TestConstants;
import com.progrexion.customermaster.ws.v4.model.Customer;

@RunWith(MockitoJUnitRunner.class)
public class CustomerMasterAdapterImplTest{
	@Mock
	private CustomerMasterConfigProperties property;
	@InjectMocks
	private CustomerMasterAdapterImpl customerMasterAdapter;
	@Mock
	private WebClient customerMasterWebClient;
	@Mock
	private WebClientHttpRequestHeader webClientHttpRequestHeader;
	@Mock
	private WebClientConfiguration webconfig;

	@Mock
	private WebClientErrorHandler webClientErrorHandler;
	@Mock
	private ObjectMapper objectMapper;
	
	@Mock
	private Customer customer;
	
	@Before
	public void setUp()
	
	{
		MockitoAnnotations.initMocks(this);


	}
	
	@Test
	public void test_getCustomerUCIDsException(){
		try
		{
			customerMasterAdapter.getCustomerUCIDs(TestConstants.UCID);
		}
		catch(BCMModuleException ex)
		{
			assertEquals(BCMModuleExceptionEnum.CUSTOMER_MASTER_500_BCM.getCode(),ex.getErrorCode());
		}
	}
	


}
