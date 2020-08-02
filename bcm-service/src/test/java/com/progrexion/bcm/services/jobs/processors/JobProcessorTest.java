package com.progrexion.bcm.services.jobs.processors;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import com.progrexion.bcm.common.jobs.enums.JobEventTypeEnum;
import com.progrexion.bcm.common.jobs.enums.JobNameEnum;
import com.progrexion.bcm.model.entities.Job;
import com.progrexion.bcm.model.entities.JobEvent;
import com.progrexion.bcm.model.repositories.JobRepository;
import com.progrexion.bcm.models.v1.JobStatusDto;
import com.progrexion.bcm.services.constants.TestConstants;




@RunWith(MockitoJUnitRunner.class)
public class JobProcessorTest {

	@InjectMocks
	private JobProcessor jobProcessor;
	@Mock
	private JobServiceFactory jobServiceFactory;
	@Mock
	private JobRepository jobRepository;
	@Mock
	private JobService ordersJobServiceImpl;
	
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void test_startJob() {
		List<JobService> jobServices = new ArrayList<>();
		jobServices.add(ordersJobServiceImpl);
		Job job = TestConstants.getNewJobObj();
		when(jobServiceFactory.selectService(Mockito.any())).thenReturn(jobServices);
		when(ordersJobServiceImpl.createJob(Mockito.any())).thenReturn(job);
		when(jobRepository.save(Mockito.any())).thenReturn(job);
		doNothing().when(ordersJobServiceImpl).start(Mockito.any(Job.class));

		List<JobStatusDto> jobStatusDtos = jobProcessor
				.startJob(JobNameEnum.RENTTRACK_REPORTED_ORDERS, "CCOM", false);
		assertNotNull(jobStatusDtos);
	}
	
	@Test
	public void test_startJob_asyncTrue() {
		List<JobService> jobServices = new ArrayList<>();
		jobServices.add(ordersJobServiceImpl);
		Job job = new Job();
		job.setJobEvents(new ArrayList<>());
		job.getJobEvents().add(new JobEvent(JobEventTypeEnum.CREATED, job));
		when(jobServiceFactory.selectService(Mockito.any())).thenReturn(jobServices);
		when(ordersJobServiceImpl.createJob(Mockito.any())).thenReturn(job);
		when(jobRepository.save(Mockito.any())).thenReturn(job);
		doNothing().when(ordersJobServiceImpl).asyncStart(Mockito.any(Job.class));

		List<JobStatusDto> jobStatusDtos = jobProcessor
				.startJob(JobNameEnum.RENTTRACK_REPORTED_ORDERS, "CCOM", true);
		assertNotNull(jobStatusDtos);
	}
	@Test
	public void test_startJob_AlreadyRunning() {
		List<JobService> jobServices = new ArrayList<>();
		jobServices.add(ordersJobServiceImpl);
		Job job = new Job();
		job.setJobEvents(new ArrayList<>());
		job.getJobEvents().add(new JobEvent(JobEventTypeEnum.STARTED, job));
		when(jobServiceFactory.selectService(Mockito.any())).thenReturn(jobServices);
		when(ordersJobServiceImpl.createJob(Mockito.any())).thenReturn(job);
		List<JobStatusDto> jobStatusDtos = jobProcessor
				.startJob(JobNameEnum.RENTTRACK_REPORTED_ORDERS, "CCOM", true);
		assertNotNull(jobStatusDtos);
	}
	@Test
	public void test_status(){
		List<JobService> jobServices = new ArrayList<>();
		jobServices.add(ordersJobServiceImpl);
		when(jobServiceFactory.selectService(Mockito.any())).thenReturn(jobServices);
		List<JobStatusDto> jobStatusDtos =  jobProcessor
				.status(JobNameEnum.RENTTRACK_REPORTED_ORDERS, "CCOM", 1l);
		assertNotNull(jobStatusDtos);
	}
	@Test
	public void test_statusWithNullJobName(){
		List<JobService> jobServices = new ArrayList<>();
		jobServices.add(ordersJobServiceImpl);
		when(jobServiceFactory.selectService(Mockito.any())).thenReturn(jobServices);
		List<JobStatusDto> jobStatusDtos =  jobProcessor
				.status(null, "CCOM", 1l);
		assertNotNull(jobStatusDtos);
	}
	@Test
	public void test_requestStop(){
		List<JobService> jobServices = new ArrayList<>();
		jobServices.add(ordersJobServiceImpl);
		when(jobServiceFactory.selectService(Mockito.any())).thenReturn(jobServices);
		List<JobStatusDto> jobStatusDtos =  jobProcessor
				.requestStop(JobNameEnum.RENTTRACK_REPORTED_ORDERS, "CCOM", 1l);
		assertNotNull(jobStatusDtos);
	}
	
}
