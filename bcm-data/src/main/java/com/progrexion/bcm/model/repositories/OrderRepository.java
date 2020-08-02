package com.progrexion.bcm.model.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.progrexion.bcm.model.entities.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

	Order findByRtOrderId(Long orderId);
}
