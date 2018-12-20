package com.gouuse.edpglobal.fs.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(HdfsConfigProperties.PREFIX)
public class HdfsConfigProperties extends BaseProperties {
	
	public static final String PREFIX = "edpglobal.fs.hdfs";
	
	/**
	 * HDFS连接地址, 默认'hdfs://localhost:8020'
	 */
	private String uri = "hdfs://localhost:8020";

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}
	
}
