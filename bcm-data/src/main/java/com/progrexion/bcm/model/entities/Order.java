package com.progrexion.bcm.model.entities;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.EnumType.STRING;

import java.io.Serializable;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.progrexion.bcm.common.enums.OrderTypeEnum;

import lombok.Data;

@Entity
@Table(name = "CUSTOMER_ORDERS")
@Access(AccessType.FIELD)
@Data
public class Order implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID",columnDefinition = "serial")
	@GeneratedValue(strategy = GenerationType.IDENTITY)	
	private Long id;
	
	@Column(name = "RT_ORDER_ID")
	private Long rtOrderId;
	
	@ManyToOne
	@JoinColumn(name = "CUSTOMER_DATA_ID", referencedColumnName = "CUSTOMER_DATA_ID")
	private BCMCustomer bcmCustomer;
	
	@Column(name = "ORDER_TYPE")
	@Enumerated(STRING)
	private OrderTypeEnum orderType;
	
	@Column(name = "STATUS")
	private String status;
	
	@Column(name = "RENT")
	private String rent;
	
	@Column(name = "OTHER")
	private String other;
	
	@Column(name = "TOTAL")
	private String total;
	
	@Column(name = "FEE")
	private String fee;
	
	@Column(name = "PAID_FOR")
	private String paidfor;
	
	@Column(name = "CREATED_DATE")
	private ZonedDateTime createdDate;

	
	@OneToMany(mappedBy = "order", cascade = ALL, fetch=FetchType.EAGER)
	private List<OrderReportedInfo>  orderReportedInfoList = new ArrayList<>();

	public void setCreatedDate() {
		this.createdDate = ZonedDateTime.now(ZoneId.systemDefault());
	}

}
