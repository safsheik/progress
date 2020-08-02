package com.progrexion.bcm.common.utils;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;


public class DateUtils {
	
	private static final ZoneId zoneId = ZoneId.systemDefault();
	
	private DateUtils() {
		
	}
	public static ZonedDateTime addDaysToTheYear(int numberOfDays) {
		LocalDate localDate = LocalDate.now(zoneId);
		return localDate.minusDays(numberOfDays).atStartOfDay(zoneId);
	}

}
