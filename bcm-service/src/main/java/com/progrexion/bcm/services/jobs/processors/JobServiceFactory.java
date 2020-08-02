package com.progrexion.bcm.services.jobs.processors;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.progrexion.bcm.common.jobs.enums.JobNameEnum;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@Service
public class JobServiceFactory {
	

	@Autowired
	@Qualifier("ordersJobServiceImpl")
	JobService ordersJobServiceImpl;

	/**
	 * Will never return null.
	 */
	public List<JobService> selectService(JobNameEnum jobName){
		log.trace("In: jobName=" + jobName);
		final List<JobService> services = new ArrayList<>();
		if (null != jobName && jobName.equals(JobNameEnum.RENTTRACK_REPORTED_ORDERS)) {
					services.add(ordersJobServiceImpl);

		} else {
			for (JobNameEnum jobName2 : JobNameEnum.values()) {
				services.addAll(selectService(jobName2));
			}
		}

		log.trace("Out: service=" + services);
		return services;
	}

}
