package com.progrexion.bcm.services.v1.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.progrexion.bcm.model.entities.CustomerActivity;
import com.progrexion.bcm.model.repositories.CustomerActivityRepository;
import com.progrexion.bcm.services.v1.CustomerActivityService;

@Service
public class CustomerActivityServiceImpl implements CustomerActivityService {

	@Autowired
	private CustomerActivityRepository customerActivityRepo;
	
	@Override
	public CustomerActivity saveCustomerActivity(CustomerActivity customerActivity) {
		customerActivity = customerActivityRepo.save(customerActivity);
		return customerActivity;
	}
	@Override
	public CustomerActivity getCustomerByCustActivityUsage(Long ucId, String brand, String custActivityUsage) {
		CustomerActivity customerActivity = null;
		Optional<CustomerActivity> opt = customerActivityRepo.findByUcIdAndBrandAndActivityUsage(ucId, brand, custActivityUsage);
		if(opt.isPresent()) {
			customerActivity = opt.get();
		}
		return customerActivity;
	}
	

	 
}
