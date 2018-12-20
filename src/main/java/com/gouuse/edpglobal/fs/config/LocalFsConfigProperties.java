package com.gouuse.edpglobal.fs.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(LocalFsConfigProperties.PREFIX)
public class LocalFsConfigProperties extends BaseProperties {
	
	public static final String PREFIX = "edpglobal.fs.local";
	
}
