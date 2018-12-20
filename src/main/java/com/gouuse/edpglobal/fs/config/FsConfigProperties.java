package com.gouuse.edpglobal.fs.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.Resource;

import com.gouuse.edpglobal.fs.beans.FileSystemType;

@ConfigurationProperties(FsConfigProperties.PREFIX)
public class FsConfigProperties {

	public static final String PREFIX = "edpglobal.fs";
	/**
	 * 当前配置，默认hdfs
	 */
	private FileSystemType current = FileSystemType.hdfs;
	
	/**
	 * 允许访问的APP
	 */
	private String[] permitApps = {"edpglobal-fs-server"};
	
	/**
	 * 默认图片
	 */
	private Resource defaultImage;

	public FileSystemType getCurrent() {
		return current;
	}

	public void setCurrent(FileSystemType current) {
		this.current = current;
	}

	public String[] getPermitApps() {
		return permitApps;
	}

	public void setPermitApps(String[] permitApps) {
		this.permitApps = permitApps;
	}

	public Resource getDefaultImage() {
		return defaultImage;
	}

	public void setDefaultImage(Resource defaultImage) {
		this.defaultImage = defaultImage;
	}
	
}
