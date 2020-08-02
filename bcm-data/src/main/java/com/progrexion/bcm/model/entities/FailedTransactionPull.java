package com.progrexion.bcm.model.entities;

import java.io.Serializable;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "FAILED_TRANSACTION_PULL")
@Access(AccessType.FIELD)
@Data
public class FailedTransactionPull implements Serializable{

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

	@Column(name = "ERROR_MESSAGE")
	private byte[] errorMessage;

	@Column(name = "CREATED_DATE")
	private ZonedDateTime createdDate;
	
	public FailedTransactionPull() {
		super();
		this.createdDate =ZonedDateTime.now(ZoneId.systemDefault());
	}
}
