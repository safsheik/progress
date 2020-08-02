package com.progrexion.bcm.models.v1;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;
@JsonInclude(JsonInclude.Include.NON_NULL) 
@Data
public class CustomerStatusResponseDto {
	
	private String status;

}
