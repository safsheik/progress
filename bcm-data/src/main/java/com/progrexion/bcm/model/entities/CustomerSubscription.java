package com.progrexion.bcm.model.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.progrexion.bcm.common.enums.SubscriptionStatusEnum;
import com.progrexion.bcm.model.common.entities.AbstractEntity;

import lombok.Data;

@Data
@Entity
@Table(name = "CUSTOMER_SUBSCRIPTION") 
public class CustomerSubscription extends AbstractEntity implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "SUBSCRIPTION_ID")
	private Long subscriptionId;

	@ManyToOne
	@JoinColumn(name = "CUSTOMER_DATA_ID", referencedColumnName = "CUSTOMER_DATA_ID")
	private BCMCustomer customer;

	@Column(name = "STATUS")
	@Enumerated(EnumType.ORDINAL)
	private SubscriptionStatusEnum status;


}
