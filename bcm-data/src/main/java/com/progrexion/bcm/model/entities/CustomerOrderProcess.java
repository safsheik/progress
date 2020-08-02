package com.progrexion.bcm.model.entities;

import java.io.Serializable;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.progrexion.bcm.common.jobs.enums.OrderProcessStatusEnum;
import com.progrexion.bcm.model.common.entities.AbstractEntity;

import lombok.Data;

@Entity
@Table(name = "CUSTOMER_ORDER_PROCESS")
@Access(AccessType.FIELD)
@Data
public class CustomerOrderProcess extends AbstractEntity implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID",columnDefinition = "serial")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "CUSTOMER_DATA_ID", referencedColumnName = "CUSTOMER_DATA_ID")
	private BCMCustomer customer;
	
	@ManyToOne
	@JoinColumn(name = "JOB_ID", referencedColumnName = "JOB_ID")
	private Job job;

	@Column(name = "STATUS")
	@Enumerated(EnumType.ORDINAL)
	private OrderProcessStatusEnum status;

}
