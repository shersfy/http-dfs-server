package com.gouuse.edpglobal.fs.service;

import java.io.InputStream;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gouuse.datahub.commons.exception.DatahubException;
import com.gouuse.datahub.commons.meta.BaseMeta;
import com.gouuse.datahub.commons.meta.FileMeta;
import com.gouuse.datahub.commons.utils.FileUtil;
import com.gouuse.edpglobal.fs.beans.HdfsMeta;
import com.gouuse.edpglobal.fs.config.FsConfigProperties;
import com.gouuse.edpglobal.fs.config.HdfsConfigProperties;
import com.gouuse.edpglobal.fs.config.LocalFsConfigProperties;
import com.gouuse.edpglobal.fs.controller.BaseController;
import com.gouuse.edpglobal.fs.types.FileSystemBase;

@Component
public class FileManager {
	
	protected static final Logger LOGGER = LoggerFactory.getLogger(BaseController.class);
	
	@Autowired
	private FsConfigProperties fsConfig;
	
	@Autowired
	private HdfsConfigProperties hdfsConfig;
	
	@Autowired
	private LocalFsConfigProperties localFsConfig;
	
	private HdfsMeta hdfsMeta;
	
	private BaseMeta baseMeta;
	
	/**
	 * 文件是否存在
	 * @param path 相对文件路径(根目录为服务器上设置的工作目录)
	 * @return
	 * @throws DatahubException
	 */
	public boolean exist(String path) throws DatahubException {
		FileSystemBase fs = getFileSystem();
		path = FileUtil.concat(fs.getWorkspace(), path);
		return fs.exist(path);
	}
	
	/**
	 * 列举出指定路径下的所有文件
	 * @param path 相对目录(根目录为服务器上设置的工作目录)
	 * @return
	 * @throws DatahubException
	 */
	public List<FileMeta> listFiles(String path) throws DatahubException {
		FileSystemBase fs = getFileSystem();
		path = FileUtil.concat(fs.getWorkspace(), path);
		List<FileMeta> files = fs.listFiles(path);
		return files;
	}
	
	/**
	 * 上传文件
	 * @param path 相对文件路径(根目录为服务器上设置的工作目录)
	 * @param input 输入流对象
	 * @return
	 * @throws DatahubException
	 */
	public String upload(String path, InputStream input) throws DatahubException {
		return upload(FilenameUtils.getName(path), path, input);
	}
	
	public String upload(String origFilename, String path, InputStream input) throws DatahubException {
		FileSystemBase fs = getFileSystem();
		String fullPath = FileUtil.concat(fs.getWorkspace(), path);
		fullPath = fs.upload(fullPath, input);
		LOGGER.info("upload file to {} '{}' --> '{}' successful", fsConfig.getCurrent().name(), origFilename, fullPath);
		return path;
	}
	
	/**
	 * 列举出指定路径下的所有文件
	 * @param path 相对文件路径(根目录为服务器上设置的工作目录)
	 * @return
	 * @throws DatahubException
	 */
	public InputStream getInputStream(String path) throws DatahubException {
		FileSystemBase fs = getFileSystem();
		path = FileUtil.concat(fs.getWorkspace(), path);
		return fs.getInputStream(path);
	}
	
	/**
	 * 获取文件系统实例
	 * @return
	 * @throws DatahubException
	 */
	public FileSystemBase getFileSystem() throws DatahubException {
		BaseMeta meta = null;
		String workspace = "";
		switch (fsConfig.getCurrent()) {
		case hdfs:
			if (hdfsMeta==null) {
				hdfsMeta = new HdfsMeta();
				hdfsMeta.setUrl(hdfsConfig.getUri());
				hdfsMeta.setUserName("hdfs");
			}
			workspace = hdfsConfig.getWorkspace();
			meta = hdfsMeta;
			break;
		default:
			if (baseMeta==null) {
				baseMeta = new BaseMeta();
			}
			workspace = localFsConfig.getWorkspace();
			meta = baseMeta;
			break;
		}
		
		FileSystemBase fs = FileSystemBase.getInstance(meta, workspace);
		return fs;
	}

}
