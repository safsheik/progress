package com.progrexion.bcm.model.common.entities;

import java.time.ZonedDateTime;

public interface DateTracked {

	ZonedDateTime getCreatedDate();
	ZonedDateTime getModifiedDate();

}
