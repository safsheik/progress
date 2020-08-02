package com.progrexion.bcm.services.v1.impl;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import com.progrexion.bcm.common.enums.CustomerActivityEnum;
import com.progrexion.bcm.model.entities.CustomerActivity;
import com.progrexion.bcm.model.repositories.CustomerActivityRepository;
import com.progrexion.bcm.services.constants.TestConstants;

@RunWith(MockitoJUnitRunner.class)
public class CustomerActivityServiceImplTest {

	@InjectMocks
	private CustomerActivityServiceImpl customerActivityService;

	@Mock
	private CustomerActivityRepository customerActivityRepo;

	@Before
	public void setUp() throws Exception {

		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void test_processCreateUserAccountSuccessTest() {
		CustomerActivity customerActivity = new CustomerActivity();
		when(customerActivityRepo.save(Mockito.any())).thenReturn(customerActivity);
		assertNotNull(customerActivityService.saveCustomerActivity(customerActivity));

	}
	@Test
	public void test_getCustomerByCustActivityUsageSuccessTest() {
		CustomerActivity customerActivity = new CustomerActivity();
		Optional<CustomerActivity> customerActivityOpt = Optional.of(customerActivity);
		when(customerActivityRepo.findByUcIdAndBrandAndActivityUsage(Mockito.any(),Mockito.any(),Mockito.any()))
		.thenReturn(customerActivityOpt);
		assertNotNull(customerActivityService.getCustomerByCustActivityUsage
				(TestConstants.UCID, 
						TestConstants.BRAND_CCOM, 
						CustomerActivityEnum.CREATE_LEASE_CUSTACTIVITY.name()));

	}


}
