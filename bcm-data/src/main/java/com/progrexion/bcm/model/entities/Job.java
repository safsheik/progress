package com.progrexion.bcm.model.entities;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.EnumType.STRING;

import java.io.Serializable;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.PostPersist;
import javax.persistence.Table;

import com.progrexion.bcm.common.jobs.enums.JobEventTypeEnum;
import com.progrexion.bcm.common.jobs.enums.JobNameEnum;
import com.progrexion.bcm.common.jobs.enums.JobStatusEnum;
import com.progrexion.bcm.model.common.entities.AbstractEntity;
import com.progrexion.bcm.model.utils.CreatedDTComparatorDesc;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "JOBS")
public class Job extends AbstractEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "JOB_ID", columnDefinition = "serial")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long jobId;
	
	@Column(name = "JOB_NAME")
	@Enumerated(STRING)
	private JobNameEnum jobName;

	@Column(name = "PERIOD_START_DATE")
	private ZonedDateTime periodStartDate;
	
	@Column(name = "PERIOD_END_DATE")
	private ZonedDateTime periodEndDate;
	
	@Column(name = "BRAND")
	private String brand;
	
	@OneToMany(mappedBy = "job", cascade = ALL, fetch=FetchType.EAGER)
	private List<JobEvent> jobEvents;
	
	@PostPersist
	public void postPersist() {
		if(this.getJobEvents() == null) {
			this.setJobEvents(new ArrayList<>());
		}
		if(this.getJobEvents().isEmpty()) {
			final JobEvent jobEvent = new JobEvent(JobEventTypeEnum.CREATED, this);
			this.getJobEvents().add(jobEvent);
		}
	}

	public Job(JobNameEnum name, String brand) {
		super();
		this.jobName = name;
		this.brand = brand;
	}
	
	public JobEvent getLatestJobEvent() {
		this.getJobEvents().sort(new CreatedDTComparatorDesc());
		return this.getJobEvents().get(0);
	}
	
	public JobStatusEnum getJobStatus() {
		return getLatestJobEvent().getJobEventCode().getJobStatus();
	}
	
	public void setPeriodEndDate() {
		this.periodEndDate = ZonedDateTime.now(ZoneId.systemDefault());
	}
}


