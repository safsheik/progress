package com.progrexion.bcm.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class ConfigManager {

	@Autowired
	private Environment env;

	public String getConfigEntry(String key) {

		return env.getProperty(key);
	}

}
