package com.progrexion.bcm.services.jobs.factory;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import com.progrexion.bcm.model.entities.Job;
import com.progrexion.bcm.model.repositories.CustomerOrderProcessRepository;
import com.progrexion.bcm.model.repositories.JobRepository;
import com.progrexion.bcm.models.v1.JobStatusDto;

public class Factory {
	
	@Autowired
	CustomerOrderProcessRepository customerOrderProcessRepo;
	
	@Autowired
	JobRepository jobRepository;
	
	public static List<JobStatusDto> convert(List<Job> jobs) {
		List<JobStatusDto> jobStatusDtos;

		if (null != jobs) {
			jobStatusDtos = jobs.stream().map(job -> convert(job)).filter(jdto -> null != jdto).collect(Collectors.toList());
		} else {
			jobStatusDtos = null;
		}

		return jobStatusDtos;
	}

	public static JobStatusDto convert(Job job) {
		JobStatusDto jobStatusDto = new JobStatusDto();
		jobStatusDto.setBrand(job.getBrand());
		jobStatusDto.setJobId(job.getJobId());
		jobStatusDto.setJobName(job.getJobName().name());		
		jobStatusDto.setStatus(job.getJobStatus().name());
		jobStatusDto.setStartTime(job.getCreatedDate());
		jobStatusDto.setEndTime(job.getModifiedDate());		
		return jobStatusDto;
	}
	
}
