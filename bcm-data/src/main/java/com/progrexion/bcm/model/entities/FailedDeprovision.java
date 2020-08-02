package com.progrexion.bcm.model.entities;

import java.io.Serializable;

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
@Table(name = "FAILED_DEPROVISION") 
public class FailedDeprovision extends AbstractEntity implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "DEPROVISION_ID",columnDefinition = "serial")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long deprovisionId;
		
	@Column(name = "UCID")
	private Long ucId;
	
	@Column(name = "BRAND")
	private String brand;
	
	@Column(name = "PBS_MESSAGE") 
	private String pbsMessage;
	
	@Column(name = "ERROR_MESSAGE")
	private String errorMessage;
	
}
