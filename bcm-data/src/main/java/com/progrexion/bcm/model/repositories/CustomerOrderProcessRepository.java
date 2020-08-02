package com.progrexion.bcm.model.repositories;

import java.util.EnumSet;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.progrexion.bcm.common.jobs.enums.OrderProcessStatusEnum;
import com.progrexion.bcm.model.entities.CustomerOrderProcess;
import com.progrexion.bcm.model.entities.Job;

@Repository
public interface CustomerOrderProcessRepository extends JpaRepository<CustomerOrderProcess, Long> { 
	List<CustomerOrderProcess> findAllByJobAndStatusIn(Job job, EnumSet<OrderProcessStatusEnum> orderStatusList);
}
