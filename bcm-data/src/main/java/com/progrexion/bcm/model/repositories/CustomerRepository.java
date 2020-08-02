package com.progrexion.bcm.model.repositories;

import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.progrexion.bcm.common.enums.SubscriptionStatusEnum;
import com.progrexion.bcm.model.entities.BCMCustomer;

@Repository
public interface CustomerRepository extends JpaRepository<BCMCustomer, Long> {
	Optional<BCMCustomer> findByCustEmail(String custEmail);
	Optional<BCMCustomer> findByUcIdOrCustEmail(Long ucId, String custEmail);
	Optional<BCMCustomer> findByUcIdAndBrand(Long ucId, String brand);
	Optional<BCMCustomer> findByCustomerDataId(Long custId);
	public List<BCMCustomer> findByUcIdIn(Collection<Long> bcmIds);
	Optional<BCMCustomer> findByUcId(Long ucId);
	Optional<BCMCustomer> findTop1ByUcIdInAndBrandOrderByCustomerDataIdDesc(Collection<Long> customerMergedIds, String brand);
	List<BCMCustomer> findAllByUcIdInAndBrand(List<Long> ucIds, String brand);
	List<BCMCustomer> findAllByUcIdInAndBrandOrderByCreatedDateDesc(List<Long> ucIds, String brand);
	List<BCMCustomer> findAllByBrandAndUcIdInOrderByCreatedDateDesc(String brand, List<Long> ucIds);	
	BCMCustomer findFirstByUcIdInAndBrandOrderByCreatedDateDesc(List<Long> ucIds, String brand);
	
	@Query(value = "select c from BCMCustomer c inner join CustomerSubscription s on "
			+ "c.customerDataId = s.customer where "
			+ "c.brand = :brand and c.residentStatus = :residentStatus and s.status= :subscriptionStatus and "
			+ "(c.transactionPullDate is null or c.transactionPullDate <= :transactionPullDate)")
	List<BCMCustomer> findAllByBrandAndTransactionPullDate(@Param("transactionPullDate") ZonedDateTime transactionPullDate,
			@Param("brand") String brand, @Param("residentStatus") int residentStatus, 
			@Param("subscriptionStatus") SubscriptionStatusEnum subscriptionStatus);
	
	@Query(value = "select cs.status from BCMCustomer bc inner join CustomerSubscription cs on "
			+ "bc.customerDataId = cs.customer where bc.ucId =:ucId and "
			+ "bc.brand = :brand and bc.residentStatus = :residentStatus order by cs.createdDate desc ")
	Page<SubscriptionStatusEnum> findCustomerSubscriptionStatusInfo(@Param("ucId") Long ucId, @Param("brand") String brand,
			@Param("residentStatus") int residentStatus, Pageable pageable);
}
