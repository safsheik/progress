package com.progrexion.bcm.model.entities;

import static javax.persistence.EnumType.STRING;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.progrexion.bcm.common.enums.OrderBureauTypeEnum;

import lombok.Data;

@Entity
@Table(name = "CUSTOMER_ORDERS_REPORTED_INFO")
@Access(AccessType.FIELD)
@Data
public class OrderReportedInfo implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID", columnDefinition = "serial")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long orderInfoId;

	@Column(name = "BUREAU_TYPE")
	@Enumerated(STRING)
	private OrderBureauTypeEnum bureauType;
	
	@Column(name = "REPORTED_AT")
	private LocalDate reportedAt;
	
	@Column(name = "CREATED_DATE")
	private ZonedDateTime createdDate;
	
	@ManyToOne
	@JoinColumn(name = "ORDER_ID", referencedColumnName = "ID")
	private Order order;

	public OrderReportedInfo() {
		this.createdDate = ZonedDateTime.now(ZoneId.systemDefault());
	}	
	public String getOrderBureauTypeEnumValue() {
		return this.bureauType.getBureauType();
	}
}
