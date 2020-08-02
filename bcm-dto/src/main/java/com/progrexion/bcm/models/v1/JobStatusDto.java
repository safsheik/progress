package com.progrexion.bcm.models.v1;

import java.io.Serializable;
import java.time.ZonedDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class JobStatusDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long jobId;
	private String status;
	private String jobName;
	@JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
	private ZonedDateTime startTime;
	@JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
	private ZonedDateTime endTime;
	private String brand;

}
