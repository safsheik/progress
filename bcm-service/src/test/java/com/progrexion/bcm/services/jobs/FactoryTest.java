package com.progrexion.bcm.services.jobs;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import com.progrexion.bcm.model.entities.Job;
import com.progrexion.bcm.models.v1.JobStatusDto;
import com.progrexion.bcm.services.constants.TestConstants;
import com.progrexion.bcm.services.jobs.factory.Factory;




@RunWith(MockitoJUnitRunner.class)
public class FactoryTest {

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void test_convert() {
		List<Job> jobs = new ArrayList<>();
		jobs.add(TestConstants.getNewJobObj());
		List<JobStatusDto> jobStatusDtos = Factory.convert(jobs);
		assertNotNull(jobStatusDtos);
	}
	@Test
	public void test_convertWithNullValues() {
		List<Job> jobs = null;
		List<JobStatusDto> jobStatusDtos = Factory.convert(jobs);
		assertEquals(null,jobStatusDtos);
	}
	@Test
	public void test_convertJob() {
		JobStatusDto jobStatusDto = Factory.convert(TestConstants.getNewJobObj());
		assertNotNull(jobStatusDto);
	}
	
}
