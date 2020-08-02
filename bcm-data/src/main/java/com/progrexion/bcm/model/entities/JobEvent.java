package com.progrexion.bcm.model.entities;

import static javax.persistence.EnumType.STRING;

import java.io.Serializable;
import java.time.ZoneId;
import java.time.ZonedDateTime;

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
import javax.persistence.Table;

import com.progrexion.bcm.common.jobs.enums.JobEventTypeEnum;
import com.progrexion.bcm.model.common.entities.AbstractEntity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;




@Data
@Entity
@Table(name = "JOB_EVENTS")
@Access(AccessType.FIELD)
@AllArgsConstructor
@NoArgsConstructor
public class JobEvent extends AbstractEntity implements Serializable{

	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "JOB_EVENT_ID",columnDefinition = "serial")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long jobEventID;

	@Column(name = "JOB_EVENT_CODE")
	@Enumerated(STRING)
	private JobEventTypeEnum jobEventCode;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "JOB_ID", referencedColumnName = "JOB_ID")
	private Job job;

	@Column(name = "DETAILS")
	private String details;
	
	@Column(name = "BRAND")
	private String brand;
	
	@Column(name = "EVENT_DATE")
	private ZonedDateTime eventDate;
	
	public JobEvent(JobEventTypeEnum jobEventCode, Job job) {
		super();
		this.jobEventCode = jobEventCode;
		this.job = job;
		this.brand = job.getBrand();
		setEventDate();
	}
	
	public void setEventDate()
	{
		this.eventDate = ZonedDateTime.now(ZoneId.systemDefault());
	}

}
