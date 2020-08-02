package com.progrexion.bcm.model.entities;

import java.io.Serializable;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.progrexion.bcm.model.common.entities.AbstractEntity;

import lombok.Data;

@Data
@Entity
@Table(name = "CUSTOMER_DATA")
public class BCMCustomer extends AbstractEntity implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "CUSTOMER_DATA_ID",columnDefinition = "serial")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long customerDataId;
	@Column(name = "UCID")
	private Long ucId;
	@Column(name = "BRAND")
	private String brand;
	@Column(name = "CUSTOMER_EMAIL")
	private String custEmail;
	@Column(name = "TRANSACTION_FINDER_ID")
	private Long transactionFinderId;
	@Column(name = "ACCESS_TOKEN")
	private String accessToken;
	@Column(name = "REFRESH_TOKEN")
	private String refreshToken;
	@Column(name = "TOKEN_EXPIRY")
	private Long tokenExpiry;
	@Column(name = "TOKEN_CREATED_DATE")
	private ZonedDateTime tokenCreatedDate;
	@Column(name = "RESIDENT_STATUS")
	private int residentStatus;
	@Column(name = "PAYMENT_ACCOUNT_ID")
	private Long paymentAccountId;	
	@Column(name = "TRANSACTION_PULL_DATE")
	private ZonedDateTime transactionPullDate;
	
	public void setTokenCreatedDate() {
		this.tokenCreatedDate = ZonedDateTime.now(ZoneId.systemDefault());
	}
	
	public void setTransactionPullDate() {
		this.transactionPullDate = ZonedDateTime.now(ZoneId.systemDefault());
	}
}
