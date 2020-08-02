package com.progrexion.bcm.services.jobs.processors;

import java.util.List;

import javax.print.attribute.standard.JobName;

import com.progrexion.bcm.common.jobs.enums.JobNameEnum;
import com.progrexion.bcm.common.model.v1.ProcessOrderMessageRequestModel;
import com.progrexion.bcm.model.entities.Job;
import com.progrexion.bcm.models.v1.JobStatusDto;




/**
 * Controls for a long running process.
 */
public interface JobService {

	/**
	 * Prepare a {@link Job} to be run.
	 * 
	 * @param brand brand identifier
	 * @return the {@link Job} identifier
	 * @if something goes wrong
	 */
	Job createJob(String brand) ;

	/**
	 * Start {@link Job} as a blocking (synchronous) process.
	 * @param job {@link Job} to be started
	 * 
	 * @if something goes wrong
	 */
	void start(Job job) ;
	
	/**
	 * Start {@link Job} as a non-blocking (asynchronous) process.
	 * @param job {@link Job} to be started
	 * 
	 * @if something goes wrong
	 */
	void asyncStart(Job job) ;

	/**
	 * Get the status of a {@link Job}.
	 * 
	 * @param brand brand identifier
	 * @param jobId {@link Job} identifier
	 * @return the current state of a running process
	 * @if something goes wrong
	 */
	List<JobStatusDto> status(String brand, Long jobId) ;
	
	/**
	 * Get the status of a {@link Job}.
	 * 
	 * @param brand brand identifier
	 * @param job {@link Job} to be started
	 * @return the current state of a running process
	 * @if something goes wrong
	 */
	JobStatusDto status(Job job) ;

	/**
	 * Record a stop request.
	 * 
	 * @param brand brand identifier
	 * @param jobId {@link Job} identifier
	 * @return the statuses of the processes that have been requested to stop
	 * @if something goes wrong
	 */
	List<JobStatusDto> requestStop(Long jobId, String brand);
	
	/**
	 * 
	 * @param jobId
	 * @param brand
	 * @return
	 * @throws PgxException
	 */
	List<JobStatusDto> requestStop(JobNameEnum jobName, Long jobId, String brand) ;

	/**
	 * Test for {@link JobEventType#INTERRUPT_REQUESTED}, and if it is the latest event, add a
	 * {@link JobEventType#INTERRUPTED} and throw a {@link JobInterruptException}.
	 * 
	 * @param job
	 *            {@link Job}
	 * @throws PgxException
	 *             if something goes wrong
	 * @throws JobInterruptException
	 *             to interrupt the job
	 */
	void performStop(Job job) /* , JobInterruptException */;

	/**
	 * Get the {@link JobName} the service is managing
	 * 
	 * @return {@link JobName}
	 */
	JobNameEnum getJobName();
	
	/**
	 * 
	 * @param jobName
	 * @param brand
	 * @param jobId
	 * @return
	 * @throws PgxException
	 */
	List<JobStatusDto> status(JobNameEnum jobName, String brand, Long jobId) ;

	void processOrderReport(ProcessOrderMessageRequestModel processOrderMessageRequestModel);

	void checkAndUpdateJobStatus(Job job);
	
}