package com.progrexion.bcm.services.jobs.processors;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.progrexion.bcm.common.exceptions.BCMModuleException;
import com.progrexion.bcm.common.jobs.enums.JobEventTypeEnum;
import com.progrexion.bcm.common.jobs.enums.JobNameEnum;
import com.progrexion.bcm.common.jobs.enums.JobStatusEnum;
import com.progrexion.bcm.model.entities.Job;
import com.progrexion.bcm.model.entities.JobEvent;
import com.progrexion.bcm.model.repositories.JobRepository;
import com.progrexion.bcm.models.v1.JobStatusDto;

import lombok.extern.slf4j.Slf4j;



@Slf4j
@Service
public class JobProcessor {

	@Autowired
	private JobServiceFactory jobServiceFactory;

	@Autowired
	private JobRepository jobRepository;


	public List<JobStatusDto> startJob(JobNameEnum jobName, String brand, boolean useAsync) {

		List<JobService> jobServices = jobServiceFactory.selectService(jobName);

		List<JobStatusDto> jobStatusDtos = new ArrayList<>();

		for (JobService jobService : jobServices) {
			log.info("service=" + jobService);
			log.info("Start " + jobName);
			log.info("create job");
			Job job = jobService.createJob(brand);

			if (job.getJobStatus().equals(JobStatusEnum.NOT_RUNNING)) {
				job.getJobEvents().add(new JobEvent(JobEventTypeEnum.STARTED, job));
				job=jobRepository.save(job);
				if (useAsync) {
					jobService.asyncStart(job);
				} else {
					jobService.start(job);
				}
			}else {
				log.warn("Job ALREADY_RUNNING");
				job.getJobEvents().add(new JobEvent(JobEventTypeEnum.ALREADY_RUNNING, job));
			}

			jobStatusDtos.add(jobService.status(job));
		}
		return jobStatusDtos;
	}

	public List<JobStatusDto> status(JobNameEnum jobName, String brand, Long jobId){
		List<JobStatusDto> jobStatusDtos = new ArrayList<>();
		if (null == jobName) {
			for (JobNameEnum jn : JobNameEnum.values()) {
				try {
					jobStatusDtos.addAll(getStatuses(jn, brand, jobId));
				} catch (BCMModuleException ex) {
					log.error("Error While retrieving processor for the Job={}.Error Message={}", jn, ex.getErrorMessage());
				}
			}
		} else {
			jobStatusDtos.addAll(getStatuses(jobName, brand, jobId));
		}
		return jobStatusDtos;
	}

	public List<JobStatusDto> requestStop(JobNameEnum jobName, String brand, Long jobId){
		List<JobStatusDto> jobStatusDtos = new ArrayList<>();
		for (JobService jobService : jobServiceFactory.selectService(jobName)) {
			log.info("service=" + jobService);
			log.info("Stop jobName: {}", jobName);
			jobStatusDtos.addAll(jobService.requestStop(jobName, jobId, brand));
			log.debug("jobDto=" + jobStatusDtos);
		}
		return jobStatusDtos;
	}

	/**
	 * Will never return null.
	 */
	private List<JobStatusDto> getStatuses(JobNameEnum jobName, String brand, Long jobId){
		List<JobStatusDto> jobStatusDtos = new ArrayList<>();
		for (JobService jobService : jobServiceFactory.selectService(jobName)) {
			log.info("jobsService=" + jobService);
			log.info("Get status of job " + jobName + " with brand='" + brand + "' and id='" + jobId + "'");
			final List<JobStatusDto> statuses = jobService.status(jobName, brand, jobId);
			jobStatusDtos.addAll(statuses);
			log.debug("jobDto=" + jobStatusDtos);
		}
		return jobStatusDtos;
	}
}
