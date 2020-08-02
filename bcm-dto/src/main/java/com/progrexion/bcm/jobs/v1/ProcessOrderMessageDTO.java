/**
 * 
 */
package com.progrexion.bcm.jobs.v1;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProcessOrderMessageDTO implements Serializable{

	private static final long serialVersionUID = 1L;
	public static final double VERSION = 1;
	
	private Long customerOrderProcessId;
	private Long customerDataId;
	private Long jobId;
	
	public ProcessOrderMessageDTO(Long customerOrderProcessId, Long customerDataId, Long jobId) {
		super();
		this.customerOrderProcessId = customerOrderProcessId;
		this.customerDataId = customerDataId;
		this.jobId = jobId;
	}
	
	

}