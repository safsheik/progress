package com.progrexion.bcm.model.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.progrexion.bcm.model.entities.BCMCustomer;
import com.progrexion.bcm.model.entities.CustomerSubscription;

@Repository
public interface CustomerSubscriptionRepository extends JpaRepository<CustomerSubscription, Long> { 
	CustomerSubscription findFirstByCustomerOrderByCreatedDateDesc(BCMCustomer customer);
	
}
