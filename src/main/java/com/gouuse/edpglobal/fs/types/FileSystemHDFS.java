package com.gouuse.edpglobal.fs.types;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import com.gouuse.datahub.commons.exception.DatahubException;
import com.gouuse.datahub.commons.meta.FileMeta;
import com.gouuse.datahub.commons.utils.FileUtil;
import com.gouuse.datahub.commons.utils.FileUtil.FileType;
import com.gouuse.edpglobal.fs.beans.HdfsMeta;
import com.gouuse.edpglobal.fs.utils.HdfsUtil;

/**
 * Hadoop分布式文件系统
 * @author pengy
 * @date 2018年12月17日
 */
public class FileSystemHDFS extends FileSystemBase {

	private FileSystem fileSystem;
	private HdfsMeta meta;

	public FileSystemHDFS(HdfsMeta meta) throws DatahubException {
		this.meta = meta;
		fileSystem = HdfsUtil.getFileSystem(meta);
		try {
			fileSystem.getStatus().getUsed();
		} catch (Exception e) {
			throw new DatahubException(e);
		}
	}

	@Override
	public String download(String path, String localDir) throws DatahubException {
		super.download(path, localDir);
		path = String.format(path, meta.getUserName());
		LOGGER.debug("download file from HDFS {}-->{}", path, localDir);
		String toFileName = FileUtil.concat(localDir, FilenameUtils.getName(path));
		HdfsUtil.copyFileToLocal(fileSystem, path, toFileName);
		return toFileName;
	}

	@Override
	public String upload(String path, InputStream input) throws DatahubException {
		if(StringUtils.isBlank(path) || input == null){
			return null;
		}
		OutputStream output = null;
		try {
			path = String.format(path, meta.getUserName());
			output = HdfsUtil.createHdfsFile(fileSystem, path, meta.getAppUser());
			IOUtils.copyLarge(input, output, new byte[2048]);
			return path;
		} catch (Throwable e) {
			if(e instanceof DatahubException){
				throw (DatahubException)e;
			}
			throw new DatahubException(e, "write input stream to HDFS error: %s", path);
		} finally {
			IOUtils.closeQuietly(output);
			IOUtils.closeQuietly(input);
		}

	}

	@Override
	public void delete(String path) throws DatahubException {
		if(StringUtils.isBlank(path)){
			return;
		}
		path = String.format(path, meta.getUserName());
		LOGGER.debug("delete file from HDFS {}", path);
		if(!HdfsUtil.deleteFile(path, fileSystem)){
			String err = String.format("delete file error from HDFS: %s", path);
			throw new DatahubException(err);
		}
	}

	@Override
	public long getFileSize(String path) {
		if(StringUtils.isBlank(path)){
			return 0;
		}

		long size = 0;
		try {
			Path hf = new Path(path);
			if(this.fileSystem.exists(hf)){
				size = this.fileSystem.getFileStatus(hf).getLen();
			}
		} catch (Exception e) {
			LOGGER.error("", e);
		}
		return size;
	}

	public FileSystem getFileSystem() {
		return fileSystem;
	}

	@Override
	public boolean exist(String path) throws DatahubException {
		try {
			Path hf = new Path(path);
			return this.fileSystem.exists(hf);
		} catch (Exception e) {
			throw new DatahubException(e);
		}
	}

	@Override
	public List<FileMeta> listFiles(String path) throws DatahubException {
		return HdfsUtil.listPath(fileSystem, path, FileType.File);
	}
	
	@Override
	public InputStream getInputStream(String path) throws DatahubException {
		try {
			return fileSystem.open(new Path(path));
		} catch (Exception e) {
			throw new DatahubException(e);
		}
	}


}
