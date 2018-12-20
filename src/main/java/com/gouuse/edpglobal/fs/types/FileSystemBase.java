package com.gouuse.edpglobal.fs.types;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gouuse.datahub.commons.exception.DatahubException;
import com.gouuse.datahub.commons.meta.BaseMeta;
import com.gouuse.datahub.commons.meta.FileMeta;
import com.gouuse.datahub.commons.utils.FileUtil;
import com.gouuse.edpglobal.fs.beans.HdfsMeta;


/**
 * 自定义文件系统抽象类
 * @author pengy
 * @date 2018年12月17日
 */
public abstract class FileSystemBase {
	
	protected static final Logger LOGGER = LoggerFactory.getLogger(FileSystemBase.class);
	
	private String workspace;
	/**
	 * 获取文件系统实例
	 * 
	 * @param meta 文件系统信息<br/>
	 * @return 文件系统实例
	 * @throws DatahubException 
	 */
	public static FileSystemBase getInstance(BaseMeta meta, String workspace) 
			throws DatahubException{
		FileSystemBase fs = null;
		if(meta instanceof HdfsMeta){
			fs = new FileSystemHDFS((HdfsMeta)meta);
		}
		if(fs == null){
			fs = new FileSystemLocal();
		}
		fs.workspace = workspace;
		return fs;
	}
	/**
	 * 从文件系统下载文件到本地文件系统, 发生异常操作失败
	 * 
	 * @param path 文件系统上的文件路径
	 * @param localDir 本地文件目录
	 * @throws DatahubException
	 */
	public String download(String path, String localDir) throws DatahubException{
		if(StringUtils.isBlank(path) || StringUtils.isBlank(localDir)){
			return null;
		}
		File dir = new File(localDir);
		if(!dir.isDirectory()){
			dir.mkdirs();
		}
		return null;
	}
	/**
	 *  从本地文件系统上传文件到目标文件系统, 发生异常操作失败
	 * 
	 * @param path 指定目标文件系统上文件路径
	 * @param input 源输入流
	 * @throws DatahubException
	 */
	public abstract String upload(String path, InputStream input) throws DatahubException;
	
	/**
	 * 获取文件信息
	 * 
	 * @param path 文件路径
	 */
	public abstract long getFileSize(String path);
	
	/**
	 * 文件是否存在
	 * 
	 * @param path 文件或目录否存在
	 * @return true 存在, false不存在
	 */
	public abstract boolean exist(String path) throws DatahubException;
	
	/**
	 * 从本地文件系统上传文件到目标文件系统, 发生异常操作失败
	 * 
	 * @param path 指定目标文件系统上文件路径
	 * @param srcFile 本地源文件
	 * @throws DatahubException
	 */
	public String upload(String path, File srcFile) throws DatahubException{
		if(srcFile == null){
			return null;
		}
		InputStream input = null;
		try {
			LOGGER.debug("upload file {}-->{}", srcFile, path);
			input = new FileInputStream(srcFile);
			return this.upload(path, input);
		} catch (Exception e) {
			if(e instanceof DatahubException){
				throw (DatahubException)e;
			}
			throw new DatahubException(e, "upload file to File System error: %s", path);
		} finally {
			IOUtils.closeQuietly(input);
		}
	}
	/**
	 * 从文件系统上删除文件, 发生异常操作失败
	 * 
	 * @author PengYang
	 * @date 2017-09-18
	 * 
	 * @param path 文件系统上的文件
	 * @throws DatahubException
	 */
	public abstract void delete(String path) throws DatahubException;
	
	public String getWorkspace() {
		return workspace;
	}
	/**获取临时文件工作路径**/
	public String getTmpWorkspace() {
		return FileUtil.concat(getWorkspace(), "tmp/upload");
	}
	
	/**
	 * 列出所有文件信息
	 * @param path 文件或目录
	 * @return
	 */
	public abstract List<FileMeta> listFiles(String path) throws DatahubException;
	
	/**
	 * 获取文件输入流
	 * @param path 文件路径
	 * @return
	 * @throws DatahubException
	 */
	public abstract InputStream getInputStream(String path) throws DatahubException;
	
}
