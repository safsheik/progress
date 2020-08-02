package com.progrexion.bcm.model.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.progrexion.bcm.common.jobs.enums.JobNameEnum;
import com.progrexion.bcm.model.entities.Job;

@Repository
public interface JobRepository extends JpaRepository<Job, Long> {

	List<Job> findByJobName(JobNameEnum jobName);

	List<Job> findByJobNameAndBrand(JobNameEnum jobName, String brand);

	Job findFirstByJobNameAndBrandOrderByCreatedDateDesc(JobNameEnum jobName, String brand);

}
