package com.progrexion.bcm.jobs.v1.controllers;

import java.util.List;

import javax.print.attribute.standard.JobName;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.progrexion.bcm.common.jobs.enums.JobNameEnum;
import com.progrexion.bcm.models.v1.JobStatusDto;
import com.progrexion.bcm.services.jobs.processors.JobProcessor;

import lombok.extern.slf4j.Slf4j;




@RestController
@RequestMapping("/v1/jobs")
@Slf4j
public class JobController {

	

	@Autowired
	JobProcessor jobProcessor;


	// Public Methods

	/**
	 * Start a {@link com.progrexion.bcm.model.entities.Job}.
	 *
	 * @param jobName (required) {@link JobName} - used to select a service
	 * @return a {@link com.progrexion.bcm.enums.JobStatus}
	 * @ if unable to perform request
	 */
	@Secured({ "ROLE_ADMIN" })
	@PostMapping(path = "/{jobName}")
	public ResponseEntity<List<JobStatusDto>> start(
			@PathVariable JobNameEnum jobName, 
			@RequestParam(value = "brand") String brand,
			@RequestParam(value = "async", required = false) Boolean async
			)  {

		log.info("Start JobName: jobName: {} and brand {}",jobName,brand);
		
		ResponseEntity<List<JobStatusDto>> responseEntity;
		boolean useAsync = async != null ? async : jobName.getAsyncDefault();

		List<JobStatusDto> jobStatusDtos = jobProcessor.startJob(jobName, brand, useAsync);
		
		log.debug("jobDto=" + jobStatusDtos);
		responseEntity = new ResponseEntity<>(jobStatusDtos, HttpStatus.OK);
		
		log.trace("ResponseEntity=" + responseEntity);
		return responseEntity;
	}
	
	/**
	 * Get the status of one or more {@link com.progrexion.bcm.model.entities.Job}(s).
	 *
	 * @param jobName (optional) {@link JobName} - if null, all {@link JobName}s will be used - used to select a service
	 * @param jobId (optional) {@link com.progrexion.bcm.model.entities.Job} identifier - if null, all {@link com.progrexion.bcm.model.entities.Job}s matching the jobName will be considered (possibly all jobs)
	 * @return a {@link ResponseEntity} of {@link JobStatusDto}s
	 * @ if unable to perform request
	 */
	@Secured({ "ROLE_ADMIN" })
	@GetMapping
	public ResponseEntity<List<JobStatusDto>> status(
			@RequestParam(value = "jobName", required = false) JobNameEnum jobName,
			@RequestParam(value = "jobId", required = false) Long jobId,
			@RequestParam(value = "brand", required = false) String brand)  {

		log.info("Start JobName: jobName: {}, JobId: {}, brand {}",jobName, jobId, brand);
		
		ResponseEntity<List<JobStatusDto>> responseEntity ;
		List<JobStatusDto> jobStatusDtos = jobProcessor.status(jobName, brand, jobId);
		responseEntity = new ResponseEntity<>(jobStatusDtos,HttpStatus.OK);
		log.trace("Out: responseEntity=" + responseEntity);
		return responseEntity;
	}

	/**
	 * Request one or more {@link com.progrexion.bcm.model.entities.Job}(s) be stopped.
	 *
	 * @param jobName (optional) {@link JobName} - if null, all {@link JobName}s will be used - used to select a service
	 * @param brand (optional) brand to work with - if null, all brands will be considered
	 * @param jobId (optional) {@link com.progrexion.bcm.model.entities.Job} identifier - if null, all {@link com.progrexion.bcm.model.entities.Job}s matching the jobName and brand will be considered (possibly all jobs)
	 * @return a {@link ResponseEntity} of {@link JobStatusDto}s
	 * @ if unable to perform request
	 */
	@Secured({ "ROLE_ADMIN" })
	@DeleteMapping
	public ResponseEntity<List<JobStatusDto>> stop(
			@RequestParam(value = "jobName", required = false) JobNameEnum jobName, 
			@RequestParam(value = "jobId", required = false) Long jobId,
			@RequestParam(value = "brand", required = false) String brand)  {

		log.info("Start JobName: jobName: {}, JobId: {}, brand {}",jobName, jobId, brand);		
		ResponseEntity<List<JobStatusDto>> responseEntity;
		List<JobStatusDto> jobStatusDto;		
		jobStatusDto = jobProcessor.requestStop(jobName, brand, jobId);
		log.debug("Result: " + jobStatusDto);
		responseEntity = new ResponseEntity<>(jobStatusDto, HttpStatus.OK);		
		log.trace("Out: responseEntity=" + responseEntity);
		return responseEntity;
	}

	/**
	 * Verify that the system is live.
	 *
	 * @return a string success message
	 */
	@GetMapping(path = "/healthcheck")
	public String test() {
		log.trace("TRACE");
		log.debug("DEBUG");
		log.info("INFO");
		log.error("ERROR");
		log.warn("WARN");
		return "Success";
	}
}
