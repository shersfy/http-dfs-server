package com.gouuse.edpglobal.fs.config;

public abstract class BaseProperties {
	
	/**
	 * 工作目录, 默认'/edpglobal/fs'
	 */
	private String workspace= "/edpglobal/fs";

	public String getWorkspace() {
		return workspace;
	}

	public void setWorkspace(String workspace) {
		this.workspace = workspace;
	}
	

}
