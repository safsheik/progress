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
@Table(name = "CUSTOMER_USAGE_ACTIVITY")
@Access(AccessType.FIELD)
@Data
public class CustomerActivity implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ACTIVITY_ID",columnDefinition = "serial")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long activityId;

	@Column(name = "UCID")
	private Long ucId;
	
	@ManyToOne
	@JoinColumn(name = "CUSTOMER_DATA_ID", referencedColumnName = "CUSTOMER_DATA_ID")
	private BCMCustomer customer;

	@Column(name = "BRAND")
	private String brand;

	@Column(name = "ACTIVITY_USAGE")
	private int activityUsage ;
	
	@Column(name = "CREATED_DATE")
	private ZonedDateTime createdDate;
	
	public CustomerActivity() {
		super();
		this.createdDate =ZonedDateTime.now(ZoneId.systemDefault());
	}
}
