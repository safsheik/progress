package com.progrexion.bcm.model.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.progrexion.bcm.model.entities.FailedDeprovision;

@Repository
public interface FailedDeprovisionRepository extends JpaRepository<FailedDeprovision, Long> { 
	
}
