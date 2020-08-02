package com.progrexion.bcm.services.jobs.processors;

import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import com.progrexion.bcm.common.jobs.enums.JobNameEnum;




@RunWith(MockitoJUnitRunner.class)
public class JobServiceFactoryTest {

	@InjectMocks
	private JobServiceFactory jobServiceFactory;
	@Mock
	private JobService ordersJobServiceImpl;
	
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void test_selectService() {
		List<JobService> services = jobServiceFactory
				.selectService(JobNameEnum.RENTTRACK_REPORTED_ORDERS);
		assertNotNull(services);
	}
	
	@Test
	public void test_selectServiceWithNull() {
		List<JobService> services = jobServiceFactory
				.selectService(null);
		assertNotNull(services);
	}
	
}
