package com.progrexion.bcm.model.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.progrexion.bcm.model.entities.CustomerActivity;

@Repository
public interface CustomerActivityRepository extends JpaRepository<CustomerActivity, Long> {
	Optional<CustomerActivity> findByUcIdAndBrandAndActivityUsage(Long ucId, String brand, String activityUsage);
}
