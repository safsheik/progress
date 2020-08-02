package com.progrexion.bcm.common.webclients.utils;

import java.io.IOException;
import java.util.Arrays;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class WebClientUtils {

	private WebClientUtils() {
	}

	public static boolean isRetryRequired(Throwable e, String adapterClass, String method, Object... errorData) {
		boolean isIOException = e instanceof IOException;
		log.error("Error occurred, Class ={}; method ={}, errordata={}, is ioException?={} errorMessage={} ",
				adapterClass, method, Arrays.toString(errorData), isIOException, e.getMessage(), e);

		return isIOException;
	}

}
