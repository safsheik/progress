package com.progrexion.bcm.messaging.models.planchange.v1;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlanChangeMessageDto {
	private Long ucid;
	private String brand;
	private String action;
	private String message;
}
