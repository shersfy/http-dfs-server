package com.gouuse.edpglobal.fs.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({FsConfigProperties.class, 
	HdfsConfigProperties.class, 
	LocalFsConfigProperties.class})
public class AppConfig {
	
}
