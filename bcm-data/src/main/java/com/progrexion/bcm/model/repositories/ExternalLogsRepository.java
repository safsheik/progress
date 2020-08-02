package com.progrexion.bcm.model.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import com.progrexion.bcm.model.entities.ExternalLog;

@Transactional(transactionManager = "platformTransactionManagerRewards")
public interface ExternalLogsRepository extends JpaRepository<ExternalLog, Long> {

}
