package com.progrexion.bcm.model.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.progrexion.bcm.common.jobs.enums.JobEventTypeEnum;
import com.progrexion.bcm.model.entities.Job;
import com.progrexion.bcm.model.entities.JobEvent;


public interface JobEventRepository extends JpaRepository<JobEvent, Long> {

    JobEvent findByJobAndJobEventCode(Job job, JobEventTypeEnum jobEventCode);
}
