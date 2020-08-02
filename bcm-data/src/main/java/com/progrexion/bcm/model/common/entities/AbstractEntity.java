package com.progrexion.bcm.model.common.entities;

import java.io.Serializable;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import lombok.Data;
@Data
@MappedSuperclass
@Access(AccessType.FIELD)
public abstract class AbstractEntity implements Serializable, DateTracked {

	private static final long serialVersionUID = 1L;

	@Column(name = "CREATED_DATE")
	protected ZonedDateTime createdDate;

	@Column(name = "MODIFIED_DATE")
	protected ZonedDateTime modifiedDate;

	public AbstractEntity() {
		super();
		this.createdDate = ZonedDateTime.now(ZoneId.systemDefault());
	}

	public void setModifiedDate() {
		this.modifiedDate = ZonedDateTime.now(ZoneId.systemDefault());
	}
	



}
