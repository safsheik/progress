package com.progrexion.bcm.services.jobs.processors;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.codehaus.plexus.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;

import com.progrexion.bcm.common.exceptions.BCMModuleException;
import com.progrexion.bcm.common.exceptions.BCMModuleExceptionEnum;
import com.progrexion.bcm.common.jobs.enums.JobEventTypeEnum;
import com.progrexion.bcm.common.jobs.enums.JobNameEnum;
import com.progrexion.bcm.common.jobs.enums.JobStatusEnum;
import com.progrexion.bcm.common.jobs.enums.OrderProcessStatusEnum;
import com.progrexion.bcm.model.entities.CustomerOrderProcess;
import com.progrexion.bcm.model.entities.Job;
import com.progrexion.bcm.model.entities.JobEvent;
import com.progrexion.bcm.model.repositories.CustomerOrderProcessRepository;
import com.progrexion.bcm.model.repositories.JobEventRepository;
import com.progrexion.bcm.model.repositories.JobRepository;
import com.progrexion.bcm.models.v1.JobStatusDto;
import com.progrexion.bcm.services.BaseBCMService;
import com.progrexion.bcm.services.jobs.factory.Factory;

import lombok.extern.slf4j.Slf4j;



/**
 * Common methods for {@link JobService} implementations.
 */
@Slf4j
public abstract class JobServiceImpl extends BaseBCMService implements JobService {

	
	@Autowired
	private JobRepository jobRepository;

	@Autowired
	private JobEventRepository jobEventRepository;
	
	@Autowired
	private CustomerOrderProcessRepository customerOrderProcessRepo;

	@Override
	public Job createJob(String brand){
		log.info("In: brand=" + brand);
		Job job = getNonTerminal(brand);
		log.info("Out: job=" + job.getJobId());
		return job;
	}

	@Override
	public List<JobStatusDto> status(String brand, Long jobId) {
		return status(getJobName(), brand, jobId);
	}
	


	@Override
	public List<JobStatusDto> status(JobNameEnum jobName, String brand, Long jobId) {
		log.info("status() : Started");
		List<JobStatusDto> jobStatusDtos;
		List<Job> jobs = new ArrayList<>();
		if (null != jobId) {
			Optional<Job> optionalJob = jobRepository.findById((jobId));
			if(optionalJob.isPresent()) {
				Job job = optionalJob.get();
				if(job.getJobName().equals(jobName) && job.getBrand().equalsIgnoreCase(brand)){
					jobs.add(job);
				}else {
					throw new BCMModuleException(BCMModuleExceptionEnum.JOB_NOT_FOUND);
				}
			}			
		
		} else {
			jobs = StringUtils.isNotBlank(brand) ? jobRepository.findByJobNameAndBrand(getJobName(), brand) :
					jobRepository.findByJobName(getJobName());
		}
		/** To check whether the job has been completed **/
		for(Job job : jobs) {
			checkAndUpdateJobStatus(job);
		}
		jobStatusDtos = Factory.convert(jobs);
		log.info("status() : Completed");
		return jobStatusDtos;
	}

	@Override
	public JobStatusDto status(Job job){
		JobStatusDto jobStatusDto = null;
		/** To check whether the job has been completed  **/
		if(null != job) {
			 checkAndUpdateJobStatus(job);
			jobStatusDto = Factory.convert(job);
		}
		return jobStatusDto;
	}

	@Override
	public List<JobStatusDto> requestStop(Long jobId, String brand){
		return requestStop(getJobName(), jobId, brand);
	}
	
	@Override
	public List<JobStatusDto> requestStop(JobNameEnum jobName, Long jobId, String brand){
		log.info("requestStop() : Started");
		List<Job> jobs = new ArrayList<>();
		if (null != jobId) {
			Optional<Job> optionalJob = jobRepository.findById((jobId));
			if (optionalJob.isPresent()) {
				Job job = optionalJob.get();
				if(job.getJobName().equals(jobName) && job.getBrand().equalsIgnoreCase(brand)){
					jobs.add(job);
				}else {
					throw new BCMModuleException(BCMModuleExceptionEnum.MATCHING_JOB_NOT_FOUND);
				}
				
			} else {
				log.info("job not found for jobId  = {}", jobId);
			}
		} else if (null != brand) {
			jobs = jobRepository.findByJobNameAndBrand(getJobName(), brand);
		} else {
			jobs = jobRepository.findByJobName(getJobName());
		}
		if (jobs.isEmpty()) {
			final String errorMessage = "job not found for jobId  = " + jobId + " or jobName = " + getJobName() + " and Brand  = " + brand;
			throw new BCMModuleException(errorMessage, BCMModuleExceptionEnum.JOB_NOT_FOUND);
		}
		log.info("requestStop() : Completed");
		return getJobStatusDtoFromJobsList(jobs, jobId, brand);
	}
	
	private List<JobStatusDto> getJobStatusDtoFromJobsList(List<Job> jobs, Long jobId, String brand ) {
		log.info("jobs size: " + jobs.size());
		List<JobStatusDto> jobStatusDtos;
		for (Job job : jobs) {
			/** if condition to check whether it either in Processing or Process Submitted */
			if (JobStatusEnum.getProcessingStates().contains(job.getJobStatus())) {
				log.info("job is processing");
				job.getJobEvents().add(new JobEvent(JobEventTypeEnum.INTERRUPT_REQUESTED, job));
			} else {
				log.info("job is NOT processing");
			}
		}
		jobRepository.saveAll(jobs); // cascades to JobEvents
		jobRepository.flush();

		jobStatusDtos = status(brand, jobId);
		return jobStatusDtos;
	}

	@Async
	@Override
	public void asyncStart(Job job){
		start(job);
	}

	// Private Methods
	
	private Job getNonTerminal(String brand){
		Job job;

		Job latestJob =jobRepository.findFirstByJobNameAndBrandOrderByCreatedDateDesc(getJobName(), brand);
		// get latest job or create a new job if none exist
		job = (null == latestJob) ? newJob(brand) : latestJob;

		// if the latest is in an 'end' state, create a new job
		final JobStatusEnum currentStatus = JobStatusEnum.valueOf(status(job).getStatus());
		/** comments */
		if (JobStatusEnum.getTerminalStates().contains(currentStatus)) {
			job = newJob(brand);
		}

		return job;
	}

	/*
	 * 
	 */
	private Job newJob(String brand) {
		Job job;

		job = new Job(getJobName(), brand);
		job.setBrand(brand);
		job.setJobName(getJobName());
		job.setPeriodStartDate(job.getCreatedDate());
		log.debug("scheduledJob=" + getJobName());
		return jobRepository.save(job);
	}

	@Override
	public void performStop(Job job) {
	
		final JobEvent jobEvent = jobEventRepository.findByJobAndJobEventCode(job,JobEventTypeEnum.INTERRUPT_REQUESTED);
		if (null!= jobEvent) {
			job.getJobEvents().add(new JobEvent(JobEventTypeEnum.INTERRUPTED, job));
			Job newJob = jobRepository.save(job);
			String errorMessage = "Job '" + newJob.getJobId() + "' interrupted.";
			throw new BCMModuleException(errorMessage, BCMModuleExceptionEnum.JOB_NOT_FOUND);
		}

	}
	
	/** 
	 * Method Checks whether all the customer orders associated with the job has been processed 
	 * Only If the the status is Process submitted 
	 * Otherwise returns the status of the job based on job events table
	 * 
	 * */
	@Override
	public void checkAndUpdateJobStatus(Job job) {
		if(job.getJobStatus().equals(JobStatusEnum.PROCESS_SUBMITTED)) {
			List<CustomerOrderProcess> customerOrderProcessList = customerOrderProcessRepo
					.findAllByJobAndStatusIn(job, OrderProcessStatusEnum.getOrderProcessStates());
			if(customerOrderProcessList.isEmpty()) {
				job.getJobEvents().add(new JobEvent(JobEventTypeEnum.COMPLETED, job));
				job.setPeriodEndDate();
				jobRepository.save(job);
			}
		} 
	}
}